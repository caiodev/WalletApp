package br.com.caiodev.walletapp.repository

import br.com.caiodev.walletapp.login.model.LoginRequest
import br.com.caiodev.walletapp.utils.BaseRepository

class LoginRepository : BaseRepository() {

    suspend fun login(loginRequest: LoginRequest): Any {
        return callApi(call = { service.loginAsync(loginRequest).await() })
    }
}