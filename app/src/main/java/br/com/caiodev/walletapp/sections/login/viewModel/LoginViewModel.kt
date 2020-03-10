package br.com.caiodev.walletapp.sections.login.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.caiodev.walletapp.sections.login.model.LoginRequest
import br.com.caiodev.walletapp.sections.login.model.repository.LoginRepository
import br.com.caiodev.walletapp.utils.base.apiResponse.BaseAPIResponse
import br.com.caiodev.walletapp.utils.dataRecoveryIds.HawkIds
import br.com.caiodev.walletapp.utils.extensions.putValueIntoHawk
import br.com.caiodev.walletapp.utils.service.APICallResult
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel : ViewModel() {

    val state = MutableLiveData<Any>()
    private val loginRepository = LoginRepository()

    fun login(loginRequest: LoginRequest) {

        viewModelScope.launch {

            val value = loginRepository.login(loginRequest)

            @Suppress("UNCHECKED_CAST")
            when (value) {

                is APICallResult.Success<*> -> with(value.data as BaseAPIResponse) {
                    putValueIntoHawk(HawkIds.userAccountData, this.data)
                    state.postValue(onLoginSuccess)
                    Timber.i("Account owner: %s", this.data.name)
                }

                is APICallResult.ProcessingError<*> -> with(value.error as BaseAPIResponse) {
                    state.postValue(this.error.message)
                }

                is APICallResult.ConnectionError -> state.postValue(onInternetConnectionError)

                else -> state.postValue(onAPIConnectionError)
            }
        }
    }

    companion object {
        const val onLoginSuccess = 0
        const val onInternetConnectionError = 1
        const val onAPIConnectionError = 2
    }
}