package br.com.caiodev.walletapp.utils.extensions

import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk

fun <T> ViewModel.getHawkValue(key: String) = Hawk.get(key) as T

fun <T> ViewModel.putValueIntoHawk(key: String, value: T) {
    Hawk.put(key, value)
}

fun ViewModel.deleteHawkValue(key: String) {
    Hawk.delete(key)
}