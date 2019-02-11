package br.com.caiodev.walletapp.sections.login.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.caiodev.walletapp.sections.login.model.LoginRequest
import br.com.caiodev.walletapp.sections.login.model.LoginResponse
import br.com.caiodev.walletapp.sections.login.model.repository.LoginRepository
import br.com.caiodev.walletapp.utils.HawkIds
import br.com.caiodev.walletapp.utils.service.APICallResult
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel : ViewModel() {

    private var userResponse: LoginResponse? = null
    private val loginRepository = LoginRepository()
    val state = MutableLiveData<Int>()
    private val coroutineContext = Job() + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    fun login(loginRequest: LoginRequest) {

        scope.launch {

            val value = loginRepository.login(loginRequest)

            when (value) {

                is APICallResult.Success<*> -> {
                    userResponse = value.data as LoginResponse
                    Hawk.put(HawkIds.userLoginResponseData, userResponse)
                    state.postValue(onLoginSuccess)
                    Timber.i("Account owner: %s", userResponse?.userAccount?.name)
                }

                else -> {
                    state.postValue(onLoginError)
                }
            }
        }
    }

    companion object {
        const val onLoginSuccess = 0
        const val onLoginError = 1
    }
}