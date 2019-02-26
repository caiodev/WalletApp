package br.com.caiodev.walletapp.sections.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.caiodev.walletapp.R
import br.com.caiodev.walletapp.sections.login.model.LoginRequest
import br.com.caiodev.walletapp.sections.login.model.LoginResponse
import br.com.caiodev.walletapp.sections.login.view_model.LoginViewModel
import br.com.caiodev.walletapp.sections.statement.view.UserAccountDetailActivity
import br.com.caiodev.walletapp.utils.BaseActivity
import br.com.caiodev.walletapp.utils.HawkIds
import br.com.caiodev.walletapp.utils.extensions.*
import br.com.caiodev.walletapp.utils.network.NetworkChecking
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LifecycleOwner {

    private lateinit var viewModel: LoginViewModel
    private val networkChecking = NetworkChecking()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(br.com.caiodev.walletapp.R.layout.activity_login)

        setupView()
        setupViewModel()
        handleViewModel()
    }

    override fun setupView() {

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

    override fun setupViewModel() {

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        viewModel.getHawkValue<LoginResponse?>(HawkIds.userLoginResponseData)?.let {
            startActivity(Intent(applicationContext, UserAccountDetailActivity::class.java))
            finish()
        }

        viewModel.getHawkValue<String?>(HawkIds.userLoginCredential)?.let { credential ->
            userLoginEditText.setText(credential)
        }
    }

    override fun handleViewModel() {

        viewModel.state.observe(this, Observer { state ->

            when (state) {

                LoginViewModel.onLoginSuccess -> {
                    setViewVisibility(loginProgressBar, View.INVISIBLE)
                    startActivity(Intent(applicationContext, UserAccountDetailActivity::class.java))
                    finish()
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

                            //Check if there is internet connection
                            if (networkChecking.isInternetConnectionAvailable(applicationContext)) {
                                //Make call
                                viewModel.login(
                                    LoginRequest(
                                        userLoginEditText.text.toString(),
                                        userPasswordEditText.text.toString()
                                    )
                                )
                            } else showSnackBar(getString(R.string.unavailable_internet_connection))
                        }

                        userPasswordEditText.text.toString().length < 6 -> {
                            setEditTextError(
                                userPasswordEditText,
                                getString(br.com.caiodev.walletapp.R.string.password_length_error)
                            )
                        }

                        else -> {
                            setEditTextError(
                                userPasswordEditText,
                                getString(br.com.caiodev.walletapp.R.string.password_regex_error)
                            )
                        }
                    }

                } else setEditTextError(
                    userPasswordEditText,
                    getString(br.com.caiodev.walletapp.R.string.empty_field_error)
                )

            } else setEditTextError(
                userLoginEditText,
                getString(br.com.caiodev.walletapp.R.string.invalid_email_or_cpf_error)
            )

        } else setEditTextError(
            userLoginEditText,
            getString(br.com.caiodev.walletapp.R.string.empty_field_error)
        )
    }

    private fun setXY(view: View, x: Float, y: Float) {
        view.scaleX = x
        view.scaleY = y
    }

    private fun setEditTextError(editText: EditText, errorMessage: String) {
        editText.error = errorMessage
    }
}