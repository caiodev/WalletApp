package br.com.caiodev.walletapp.utils.extensions

import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk

@Suppress("unused")
fun <T> ViewModel.getHawkValue(key: String) = Hawk.get(key) as T

@Suppress("unused")
fun <T> ViewModel.putValueIntoHawk(key: String, value: T) {
    Hawk.put(key, value)
}

@Suppress("unused")
fun ViewModel.deleteHawkValue(key: String) {
    Hawk.delete(key)
}

@Suppress("unused")
inline fun <reified T> ViewModel.castAttribute(attribute: Any) = attribute as T