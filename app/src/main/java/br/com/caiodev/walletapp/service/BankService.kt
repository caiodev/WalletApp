package br.com.caiodev.walletapp.service

import br.com.caiodev.walletapp.login.model.LoginRequest
import br.com.caiodev.walletapp.login.model.LoginResponse
import br.com.caiodev.walletapp.statements.model.UserStatement
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BankService {

    @POST("login")
    fun login(@Body request: LoginRequest): Deferred<LoginResponse>

    @GET("statements/1")
    fun getUserDetails(): Deferred<UserStatement>
}