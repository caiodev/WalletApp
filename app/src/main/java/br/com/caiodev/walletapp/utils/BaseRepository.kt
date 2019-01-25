package br.com.caiodev.walletapp.utils

import br.com.caiodev.walletapp.factory.RetrofitService
import br.com.caiodev.walletapp.service.BankService
import retrofit2.Response
import timber.log.Timber
import java.net.UnknownHostException

open class BaseRepository {

    private var count = 0

    val service: BankService = RetrofitService().createService()

    suspend fun <T : Any> callApi(
        call: suspend () -> Response<T>
    ): Any {

        try {

            count++
            Timber.i("NumeroDeVezes: $count")

            val response = call.invoke()

            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    return apiResponse
                }
            }
        } catch (exception: UnknownHostException) {
            return true
        }

        return true
    }
}