package br.com.caiodev.walletapp.sections.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.caiodev.walletapp.R
import br.com.caiodev.walletapp.sections.login.model.LoginRequest
import br.com.caiodev.walletapp.sections.login.model.UserAccount
import br.com.caiodev.walletapp.sections.login.viewModel.LoginViewModel
import br.com.caiodev.walletapp.sections.userStatements.view.UserAccountDetailsActivity
import br.com.caiodev.walletapp.utils.base.ActivityFlow
import br.com.caiodev.walletapp.utils.dataRecoveryIds.HawkIds
import br.com.caiodev.walletapp.utils.extensions.*
import br.com.caiodev.walletapp.utils.network.NetworkChecking
import br.com.caiodev.walletapp.utils.text.TextValidation
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), ActivityFlow {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupView()
        handleViewModel()
    }

    override fun setupView() {

        viewModel.getHawkValue<String?>(HawkIds.userLoginCredential)?.let { credential ->
            userLoginEditText.setText(credential)
        }

        userLoginEditText.setOnClickListener {
            userLoginEditText.isCursorVisible = true
        }

        userPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(applicationContext, userPasswordEditText)
                loginValidation()
                true
            } else {
                false
            }
        }

        loginButton.setOnClickListener {
            loginValidation()
        }
    }

    override fun handleViewModel() {

        viewModel.getHawkValue<UserAccount?>(HawkIds.userAccountData)?.let {
            startActivity(Intent(applicationContext, UserAccountDetailsActivity::class.java))
            finish()
        }

        viewModel.state.observe(this, Observer { state ->

            when (state) {

                LoginViewModel.onLoginSuccess -> {
                    setViewVisibility(loginProgressBar, View.INVISIBLE)
                    startActivity(
                        Intent(
                            applicationContext,
                            UserAccountDetailsActivity::class.java
                        )
                    )
                    finish()
                }

                is String -> {
                    changeViewPropertiesWhenThereIsAnErrorRelatedToTheAPI(state)
                }

                LoginViewModel.onInternetConnectionError -> {
                    changeViewPropertiesWhenThereIsAnErrorRelatedToTheAPI(
                        getString(R.string.unavailable_internet_connection_error)
                    )
                }

                LoginViewModel.onAPIConnectionError -> {
                    changeViewPropertiesWhenThereIsAnErrorRelatedToTheAPI(
                        getString(R.string.unreachable_api_error)
                    )
                }
            }
        })
    }

    private fun loginValidation() {

        if (userLoginEditText.text.toString().isNotEmpty()) {

            if (TextValidation.validateEmail(userLoginEditText.text.toString()) ||
                TextValidation.validateCPF(userLoginEditText.text.toString())
            ) {

                if (userPasswordEditText.text.toString().isNotEmpty()) {

                    when {

                        TextValidation.validatePassword(userPasswordEditText.text.toString()) -> {

                            loginButton.isClickable = false
                            setViewXYScales(loginButton, 0f, 0f)
                            setViewXYScales(loginProgressBar, 1f, 1f)

                            //Save Email or CPF on Hawk
                            viewModel.putValueIntoHawk(
                                HawkIds.userLoginCredential,
                                userLoginEditText.text.toString()
                            )

                            //Check if there is internet connection
                            if (NetworkChecking.isInternetConnectionAvailable(applicationContext)) {
                                //Make call
                                viewModel.login(
                                    LoginRequest(
                                        userLoginEditText.text.toString(),
                                        userPasswordEditText.text.toString()
                                    )
                                )
                            } else showSnackBar(
                                this,
                                getString(R.string.unavailable_internet_connection_error)
                            )
                        }

                        userPasswordEditText.text.toString().length < 6 -> {
                            setEditTextError(
                                userPasswordEditText,
                                getString(R.string.password_length_error)
                            )
                        }

                        else -> {
                            setEditTextError(
                                userPasswordEditText,
                                getString(R.string.password_regex_error)
                            )
                        }
                    }

                } else setEditTextError(
                    userPasswordEditText,
                    getString(R.string.empty_field_error)
                )

            } else setEditTextError(
                userLoginEditText,
                getString(R.string.invalid_email_or_cpf_error)
            )

        } else setEditTextError(
            userLoginEditText,
            getString(R.string.empty_field_error)
        )
    }

    private fun changeViewPropertiesWhenThereIsAnErrorRelatedToTheAPI(snackBarText: String) {
        loginButton.isClickable = true
        setViewXYScales(loginButton, 1f, 1f)
        setViewXYScales(loginProgressBar, 0f, 0f)
        showSnackBar(this, snackBarText)
    }
}