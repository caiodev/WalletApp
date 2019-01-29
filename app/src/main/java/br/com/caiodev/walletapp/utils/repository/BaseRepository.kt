package br.com.caiodev.walletapp.utils.repository

import br.com.caiodev.walletapp.utils.factory.RetrofitService
import br.com.caiodev.walletapp.utils.service.APICallResult
import br.com.caiodev.walletapp.utils.service.BankService
import retrofit2.Response
import java.net.UnknownHostException

open class BaseRepository {

    val service: BankService = RetrofitService().createService()

    suspend fun <T : Any> callApi(
        call: suspend () -> Response<T>
    ): Any {

        try {

            val response = call.invoke()

            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    return APICallResult.Success(apiResponse)
                }
            }

        } catch (exception: UnknownHostException) {
            return APICallResult.Error
        }

        return APICallResult.Error
    }
}