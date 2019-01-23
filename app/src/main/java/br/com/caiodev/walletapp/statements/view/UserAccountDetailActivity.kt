package br.com.caiodev.walletapp.statements.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import br.com.caiodev.walletapp.R
import br.com.caiodev.walletapp.extensions.setViewVisibility
import br.com.caiodev.walletapp.login.model.LoginResponse
import br.com.caiodev.walletapp.login.view.LoginActivity
import br.com.caiodev.walletapp.statements.viewModel.StatementViewModel
import br.com.caiodev.walletapp.statements.viewModel.ViewModelDataHelper
import br.com.caiodev.walletapp.utils.HawkIds
import br.com.caiodev.walletapp.utils.MasterActivity
import kotlinx.android.synthetic.main.activity_user_account_detail.*

class UserAccountDetailActivity : MasterActivity() {

    private lateinit var viewModel: StatementViewModel
    private val viewModelDataHelper = ViewModelDataHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account_detail)

        setupView()
        setupViewModel()
        bindViewToViewModel()
    }

    override fun setupView() {
        setViewVisibility(statementListProgressBar, View.VISIBLE)
        accountOwnerStatementsRecyclerView.setHasFixedSize(true)
        statementListSwipeRefreshLayout.setColorSchemeResources(R.color.purple)
    }

    override fun setupViewModel() {

        viewModel = ViewModelProviders.of(this).get(StatementViewModel::class.java)

        viewModel.getHawkValue<LoginResponse>(HawkIds.userLoginResponseData).userAccount?.apply {
            accountOwnerNameTextView.text = name
            accountNumberTextView.text = bankAccount

            accountBalanceTextView.text =
                    String.format(getString(R.string.balance_placeholder), balance.toString())
        }

        logoutImageView.setOnClickListener {
            viewModel.deleteHawkValue(HawkIds.userLoginResponseData)
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

        viewModel.getStatement()

        statementListSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getStatement()
        }
    }

    override fun bindViewToViewModel() {

        viewModel.state.observe(this, Observer { state ->

            when (state) {

                StatementViewModel.onStatementRetrievalSuccess -> {
                    viewModelDataHelper.listReceiver(viewModel.getStatementList())
                    accountOwnerStatementsRecyclerView.adapter =
                            StatementAdapter(viewModelDataHelper)

                    runLayoutAnimation(accountOwnerStatementsRecyclerView)
                }

                StatementViewModel.onStatementRetrievalError -> {

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

        if (statementListSwipeRefreshLayout.isRefreshing) statementListSwipeRefreshLayout.isRefreshing =
                false

        if (statementListProgressBar.visibility == View.VISIBLE) setViewVisibility(
            statementListProgressBar,
            View.GONE
        )
    }
}