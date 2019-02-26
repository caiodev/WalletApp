package br.com.caiodev.walletapp.utils.network

import android.content.Context
import android.net.ConnectivityManager

class NetworkChecking {

    fun isInternetConnectionAvailable(context: Context): Boolean {
        with(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
            activeNetworkInfo?.let {
                return it.isConnected
            }
        }

        return false
    }
}