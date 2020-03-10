package br.com.caiodev.walletapp.sections.userStatements.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import br.com.caiodev.walletapp.R
import br.com.caiodev.walletapp.sections.login.model.UserAccount
import br.com.caiodev.walletapp.sections.login.view.LoginActivity
import br.com.caiodev.walletapp.sections.userStatements.model.StatementAdapter
import br.com.caiodev.walletapp.sections.userStatements.viewModel.UserAccountDetailsViewModel
import br.com.caiodev.walletapp.utils.base.ActivityFlow
import br.com.caiodev.walletapp.utils.dataRecoveryIds.HawkIds
import br.com.caiodev.walletapp.utils.extensions.*
import br.com.caiodev.walletapp.utils.network.NetworkChecking
import br.com.caiodev.walletapp.utils.text.TextFormatting
import kotlinx.android.synthetic.main.activity_user_account_detail.*

class UserAccountDetailsActivity : AppCompatActivity(), ActivityFlow {

    private val viewModel: UserAccountDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(UserAccountDetailsViewModel::class.java)
    }
    private val textFormatting = TextFormatting()
    private var statementAdapter: StatementAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account_detail)
        setupView()
        handleViewModel()
    }

    override fun setupView() {

        setViewVisibility(statementListProgressBar, View.VISIBLE)
        statementListSwipeRefreshLayout.setColorSchemeResources(R.color.purple)
        accountOwnerRecyclerView.setHasFixedSize(true)

        motionLayout.setTransitionListener(
            object : MotionLayout.TransitionListener {

                override fun allowsTransition(p0: MotionScene.Transition?): Boolean {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onTransitionTrigger(
                    p0: MotionLayout?,
                    p1: Int,
                    p2: Boolean,
                    p3: Float
                ) {

                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

                }

                /**
                There is a little problem with MotionLayout. When animations start,
                ProgressBar visibility is set to VISIBLE instead of just letting it the way it is
                even though there's no code to change its visibility, so a temporary workaround
                is to set the ProgressBar visibility back to GONE using MotionLayout listeners
                 */
                override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                    setViewVisibility(statementListProgressBar, View.GONE)
                }

                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                    setViewVisibility(statementListProgressBar, View.GONE)
                }
            })

        viewModel.getHawkValue<UserAccount>(HawkIds.userAccountData).apply {

            accountOwnerNameTextView.text = name

            accountNumberTextView.text =
                textFormatting.concatenateStrings(
                    getString(R.string.account_header),
                    bankAccount
                )

            accountBalanceTextView.text =
                textFormatting.concatenateStrings(
                    getString(R.string.balance_header),
                    textFormatting.formatCurrency(balance)
                )
        }

        logoutImageView.setOnClickListener {
            viewModel.deleteHawkValue(HawkIds.userAccountData)
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        statementListSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkChecking.isInternetConnectionAvailable(applicationContext)) {
                viewModel.getStatement()
            } else {
                setViewXYScales(
                    offlineImageView,
                    1f,
                    1f
                )
                offlineImageView.setImageResource(R.drawable.ic_no_internet_connection)
            }

            showSnackBar(
                this,
                getString(R.string.unavailable_internet_connection_error)
            )
            setViewVisibility(statementListSwipeRefreshLayout)
        }
    }

    override fun handleViewModel() {

        viewModel.getDataTypeState().observe(this, Observer { state ->

            when (state) {

                is MutableList<*> -> {

                    statementAdapter?.let {
                        it.updateDataSource(viewModel.castAttribute(state))
                        runLayoutAnimation(accountOwnerRecyclerView)
                    } ?: run {
                        statementAdapter =
                            StatementAdapter(
                                viewModel.castAttribute(state)
                            )
                        accountOwnerRecyclerView.adapter = statementAdapter
                        runLayoutAnimation(accountOwnerRecyclerView)
                    }
                }

                is String -> showSnackBar(this, state)

                UserAccountDetailsViewModel.onInternetConnectionError -> {
                    setViewVisibility(statementListProgressBar, View.GONE)
                    setViewVisibility(statementListSwipeRefreshLayout)
                    showSnackBar(
                        this,
                        getString(R.string.unavailable_internet_connection_error)
                    )
                }

                UserAccountDetailsViewModel.onAPIConnectionError -> {
                    setViewVisibility(statementListProgressBar, View.GONE)
                    setViewVisibility(statementListSwipeRefreshLayout)
                    showSnackBar(this, getString(R.string.unreachable_api_error))
                }
            }
        })
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {

        val controller =
            AnimationUtils.loadLayoutAnimation(
                recyclerView.context, R.anim.layout_animation_fall_down
            )

        recyclerView.layoutAnimation = controller
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
        setViewVisibility(statementListSwipeRefreshLayout)

        if (statementListProgressBar.visibility == View.VISIBLE) setViewVisibility(
            statementListProgressBar,
            View.GONE
        )
    }
}