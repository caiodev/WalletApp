package br.com.caiodev.walletapp.sections.statement.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
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
import br.com.caiodev.walletapp.utils.ViewType
import br.com.caiodev.walletapp.utils.extensions.deleteHawkValue
import br.com.caiodev.walletapp.utils.extensions.getHawkValue
import br.com.caiodev.walletapp.utils.network.NetworkChecking
import kotlinx.android.synthetic.main.activity_user_account_detail.*

class UserAccountDetailActivity : BaseActivity(), LifecycleOwner {

    private lateinit var viewModel: StatementViewModel
    private val viewModelDataHelper = ViewModelDataHelper()
    private var textFormatting: TextFormatting? = null
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
    }

    override fun setupViewModel() {

        viewModel = ViewModelProviders.of(this).get(StatementViewModel::class.java)

        viewModel.getHawkValue<LoginResponse>(HawkIds.userLoginResponseData).userAccount.apply {
            accountOwnerNameTextView.text = name
            accountNumberTextView.text = bankAccount

            textFormatting = TextFormatting()
            accountBalanceTextView.text = textFormatting?.formatCurrency(balance)
        }

        logoutImageView.setOnClickListener {
            viewModel.deleteHawkValue(HawkIds.userLoginResponseData)
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        statementListSwipeRefreshLayout.setOnRefreshListener {

            if (networkChecking.checkInternetConnection(applicationContext)) {
                viewModel.getStatement()
            } else showSnackBar(getString(R.string.unavailable_internet_connection))
        }
    }

    override fun handleViewModel() {

        if (networkChecking.checkInternetConnection(applicationContext)) {

            viewModel.getStatement()

            viewModel.state.observe(this, Observer { state ->

                when (state) {

                    is MutableList<*> -> {

                        viewModelDataHelper.getList(state.filterIsInstance<ViewType>() as MutableList<ViewType>)

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
        } else showSnackBar(getString(R.string.unavailable_internet_connection))
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