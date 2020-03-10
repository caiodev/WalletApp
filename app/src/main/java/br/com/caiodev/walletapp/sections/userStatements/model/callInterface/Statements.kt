package br.com.caiodev.walletapp.sections.userStatements.model.callInterface

import br.com.caiodev.walletapp.utils.base.apiResponse.BaseAPIResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Statements {
    @GET("statements/{userId}")
    fun getUserDetailsAsync(@Path("userId") userId: Int):
            Deferred<Response<BaseAPIResponse>>
}