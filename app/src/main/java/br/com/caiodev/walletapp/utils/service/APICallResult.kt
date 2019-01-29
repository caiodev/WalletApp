package br.com.caiodev.walletapp.utils.service

sealed class APICallResult<out T: Any> {
    data class Success<out T : Any>(val data: T) : APICallResult<T>()
    object Error : APICallResult<Nothing>()
}