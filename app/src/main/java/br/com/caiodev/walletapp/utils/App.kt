package br.com.caiodev.walletapp.utils

import android.app.Application
import br.com.caiodev.walletapp.BuildConfig
import com.orhanobut.hawk.Hawk
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Hawk.init(this).build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}