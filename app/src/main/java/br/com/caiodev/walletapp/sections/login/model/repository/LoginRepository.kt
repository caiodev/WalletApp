package br.com.caiodev.walletapp.sections.login.model.repository

import br.com.caiodev.walletapp.sections.login.model.LoginRequest
import br.com.caiodev.walletapp.utils.repository.BaseRepository

class LoginRepository : BaseRepository() {

    suspend fun login(loginRequest: LoginRequest): Any {
        return callApi(call = { service.loginAsync(loginRequest).await() })
    }
}