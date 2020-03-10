package br.com.caiodev.walletapp.utils.service

sealed class APICallResult<out T : Any> {
    //When everything works fine and the API returns a valid result
    data class Success<out T : Any>(val data: T) : APICallResult<T>()

    //When the user request is not successfully completed. e.g When a record is nowhere to be found
    //(It could return 404) but the API creator decided not to because, why not ¯\_(ツ)_/¯
    data class ProcessingError<out T : Any>(val error: T) : APICallResult<T>()

    //When there's no internet connection (Catching the following exceptions: UnknownHost and Timeout)
    object ConnectionError : APICallResult<Nothing>()

    //When the problem is on the server side and it's not reachable
    object UnsuccessfulCall : APICallResult<Nothing>()
}