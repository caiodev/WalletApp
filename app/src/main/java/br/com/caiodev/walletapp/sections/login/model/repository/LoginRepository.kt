package br.com.caiodev.walletapp.sections.login.model.repository

import br.com.caiodev.walletapp.sections.login.model.LoginRequest
import br.com.caiodev.walletapp.sections.login.model.callInterface.Login
import br.com.caiodev.walletapp.utils.base.RemoteRepository
import br.com.caiodev.walletapp.utils.factory.RetrofitService

class LoginRepository : RemoteRepository() {

    private val retrofitService = RetrofitService()

    suspend fun login(loginRequest: LoginRequest): Any {
        return callApi(call = {
            retrofitService.getRetrofitService<Login>().loginAsync(loginRequest).await()
        })
    }
}