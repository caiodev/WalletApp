package br.com.caiodev.walletapp.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.caiodev.walletapp.login.viewModel.LoginViewModel
import br.com.caiodev.walletapp.statements.view.UserAccountDetailActivity
import br.com.caiodev.walletapp.utils.HawkIds
import br.com.caiodev.walletapp.utils.MasterActivity
import br.com.caiodev.walletapp.utils.TextValidation.validateCPF
import br.com.caiodev.walletapp.utils.TextValidation.validateEmail
import br.com.caiodev.walletapp.utils.TextValidation.validatePassword
import br.com.caiodev.walletapp.R
import br.com.caiodev.walletapp.login.model.LoginRequest
import br.com.caiodev.walletapp.login.model.LoginResponse
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : MasterActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupView()
        setupViewModel()
        bindViewToViewModel()
    }

    override fun setupView() {

        userLoginEditText.setOnClickListener {
            userLoginEditText.isCursorVisible = true
        }

        loginButton.setOnClickListener {
            loginValidation()
        }
    }

    override fun setupViewModel() {

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        viewModel.getHawkValue<LoginResponse?>(HawkIds.userLoginResponseData)?.let {
            startActivity(Intent(applicationContext, UserAccountDetailActivity::class.java))
        }

        viewModel.getHawkValue<String?>(HawkIds.userLoginCredential)?.let { credential ->
            userLoginEditText.setText(credential)
        }
    }

    override fun bindViewToViewModel() {

        viewModel.state.observe(this, Observer { state ->

            when (state) {

                LoginViewModel.onLoginSuccess -> {
                    setViewVisibility(loginProgressBar, View.GONE)
                    startActivity(Intent(applicationContext, UserAccountDetailActivity::class.java))
                }

                LoginViewModel.onLoginError -> {
                    setViewVisibility(loginButton, View.VISIBLE)
                    loginButton.isClickable = true
                    setXY(loginButton, 1f, 1f)
                    setXY(loginProgressBar, 0f, 0f)
                }
            }
        })
    }

    private fun loginValidation() {

        if (userLoginEditText.text.toString().isNotEmpty()) {

            if (validateEmail(userLoginEditText.text.toString()) ||
                validateCPF(userLoginEditText.text.toString())
            ) {

                if (userPasswordEditText.text.toString().isNotEmpty()) {

                    when {

                        validatePassword(userPasswordEditText.text.toString()) -> {

                            loginButton.isClickable = false
                            setXY(loginButton, 0f, 0f)
                            setXY(loginProgressBar, 1f, 1f)

                            //Save Email or CPF on Hawk
                            viewModel.putValueIntoHawk(
                                HawkIds.userLoginCredential,
                                userLoginEditText.text.toString()
                            )

                            //Make call
                            viewModel.login(
                                LoginRequest(
                                    userLoginEditText.text.toString(),
                                    userPasswordEditText.text.toString()
                                )
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

                } else setEditTextError(userPasswordEditText, getString(R.string.empty_field_error))

            } else setEditTextError(
                userLoginEditText,
                getString(R.string.invalid_email_or_cpf_error)
            )

        } else setEditTextError(userLoginEditText, getString(R.string.empty_field_error))
    }

    private fun setXY(view: View, x: Float, y: Float) {
        view.scaleX = x
        view.scaleY = y
    }

    private fun setEditTextError(editText: EditText, errorMessage: String) {
        editText.error = errorMessage
    }

    private fun setViewVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }
}