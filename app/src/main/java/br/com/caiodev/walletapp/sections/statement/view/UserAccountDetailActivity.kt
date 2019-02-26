package br.com.caiodev.walletapp.sections.statement.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import br.com.caiodev.walletapp.R
import br.com.caiodev.walletapp.sections.login.model.LoginResponse
import br.com.caiodev.walletapp.sections.login.view.LoginActivity
import br.com.caiodev.walletapp.sections.statement.view_model.StatementViewModel
import br.com.caiodev.walletapp.sections.statement.view_model.ViewModelDataHelper
import br.com.caiodev.walletapp.utils.BaseActivity
import br.com.caiodev.walletapp.utils.HawkIds
import br.com.caiodev.walletapp.utils.TextFormatting
import br.com.caiodev.walletapp.utils.extensions.castAttribute
import br.com.caiodev.walletapp.utils.extensions.deleteHawkValue
import br.com.caiodev.walletapp.utils.extensions.getHawkValue
import br.com.caiodev.walletapp.utils.network.NetworkChecking
import kotlinx.android.synthetic.main.activity_user_account_detail.*

class UserAccountDetailActivity : BaseActivity(), LifecycleOwner {

    private lateinit var viewModel: StatementViewModel
    private val viewModelDataHelper = ViewModelDataHelper()
    private val textFormatting = TextFormatting()
    private var statementAdapter: StatementAdapter? = null
    private val networkChecking = NetworkChecking()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account_detail)

        setupView()
        setupViewModel()
        handleViewModel()
    }

    override fun setupView() {
        setViewVisibility(statementListProgressBar, View.VISIBLE)
        accountOwnerRecyclerView.setHasFixedSize(true)
        statementListSwipeRefreshLayout.setColorSchemeResources(R.color.purple)

        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            /**
                There is a little problem with MotionLayout. When animations start,
                ProgressBar visibility is set to VISIBLE instead of just letting it the way it is
                even though there is not a code to change its visibility,so a temporary workaround
                is to set the ProgressBar visibility back to GONE using MotionLayout listeners
            */
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                setViewVisibility(statementListProgressBar, View.GONE)
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                setViewVisibility(statementListProgressBar, View.GONE)
            }
        })
    }

    override fun setupViewModel() {

        viewModel = ViewModelProviders.of(this).get(StatementViewModel::class.java)

        viewModel.getHawkValue<LoginResponse>(HawkIds.userLoginResponseData).userAccount.apply {

            accountOwnerNameTextView.text = name

            accountNumberTextView.text =
                textFormatting.concatenateStrings(getString(R.string.account_header), bankAccount)

            accountBalanceTextView.text = textFormatting.concatenateStrings(
                getString(R.string.balance_header),
                textFormatting.formatCurrency(balance)
            )
        }

        logoutImageView.setOnClickListener {
            viewModel.deleteHawkValue(HawkIds.userLoginResponseData)
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        statementListSwipeRefreshLayout.setOnRefreshListener {
            if (networkChecking.isInternetConnectionAvailable(applicationContext)) {
                viewModel.getStatement()
            } else
                showSnackBar(getString(R.string.unavailable_internet_connection))
            dismissSwipeRefreshLayoutLoading(statementListSwipeRefreshLayout)
        }
    }

    override fun handleViewModel() {

        //Implement reactive connection detection to recall this method when internet connection is available
        viewModel.getDataTypeState().observe(this, Observer { state ->

            when (state) {

                is MutableList<*> -> {

                    viewModelDataHelper.getList(viewModel.castAttribute(state))

                    statementAdapter?.let {
                        runLayoutAnimation(accountOwnerRecyclerView)
                    } ?: run {
                        statementAdapter = StatementAdapter(viewModelDataHelper)
                        accountOwnerRecyclerView.adapter = statementAdapter
                        runLayoutAnimation(accountOwnerRecyclerView)
                    }
                }

                else -> {
                    setViewVisibility(statementListProgressBar, View.GONE)
                    dismissSwipeRefreshLayoutLoading(statementListSwipeRefreshLayout)
                    showSnackBar(getString(R.string.unavailable_internet_connection))
                }
            }
        })
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {

        val controller =
            AnimationUtils.loadLayoutAnimation(
                recyclerView.context,
                R.anim.layout_animation_fall_down
            )

        recyclerView.layoutAnimation = controller
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
        dismissSwipeRefreshLayoutLoading(statementListSwipeRefreshLayout)

        if (statementListProgressBar.visibility == View.VISIBLE) setViewVisibility(
            statementListProgressBar,
            View.GONE
        )
    }
}