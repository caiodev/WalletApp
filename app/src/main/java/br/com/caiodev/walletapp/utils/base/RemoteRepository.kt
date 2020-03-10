package br.com.caiodev.walletapp.utils.base

import br.com.caiodev.walletapp.utils.service.APICallResult
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

open class RemoteRepository {

    private var errorResponse = Any()

    //This is the internal custom error code for this API
    private val apiErrorCode = 53

    protected suspend fun <T : Any> callApi(
        call: suspend () -> Response<T>
    ): Any {

        try {

            val response = call.invoke()

            if (response.isSuccessful) {

                response.body()?.let { apiResponse ->
                    return APICallResult.Success(
                        apiResponse
                    )
                }
            } else return APICallResult.UnsuccessfulCall

        } catch (exception: Exception) {
            when (exception) {
                is UnknownHostException, is SocketTimeoutException, is ConnectException,
                is SSLHandshakeException -> return APICallResult.ConnectionError
            }
        }

        return APICallResult.ProcessingError(errorResponse)
    }
}