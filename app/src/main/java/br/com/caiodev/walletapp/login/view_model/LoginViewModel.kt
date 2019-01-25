package br.com.caiodev.walletapp.login.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.caiodev.walletapp.login.model.LoginRequest
import br.com.caiodev.walletapp.login.model.LoginResponse
import br.com.caiodev.walletapp.repository.LoginRepository
import br.com.caiodev.walletapp.utils.HawkIds
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

                is Boolean -> {
                    state.postValue(onLoginError)
                }

                else -> {
                    if (value is LoginResponse) {
                        userResponse = value
                        Hawk.put(HawkIds.userLoginResponseData, userResponse)
                        state.postValue(onLoginSuccess)
                        Timber.i("Account owner: %s", userResponse?.userAccount?.name)
                    }
                }
            }
        }
    }

    fun <T> getHawkValue(key: String) = Hawk.get(key) as T

    fun <T> putValueIntoHawk(key: String, value: T) {
        Hawk.put(key, value)
    }

    companion object {
        const val onLoginSuccess = 0
        const val onLoginError = 1
    }
}