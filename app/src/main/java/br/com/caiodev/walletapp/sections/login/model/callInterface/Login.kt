package br.com.caiodev.walletapp.sections.login.model.callInterface

import br.com.caiodev.walletapp.sections.login.model.LoginRequest
import br.com.caiodev.walletapp.sections.login.model.UserAccount
import br.com.caiodev.walletapp.utils.base.apiResponse.BaseAPIResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Login {

    @POST("login")
    fun loginAsync(@Body request: LoginRequest):
            Deferred<Response<BaseAPIResponse>>
}