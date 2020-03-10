package br.com.caiodev.walletapp.utils.init

import android.app.Application
import br.com.caiodev.walletapp.BuildConfig
import com.orhanobut.hawk.Hawk
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

@Suppress("UNUSED")
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Hawk.init(this).build()

        if (BuildConfig.DEBUG) {
            LeakCanary.install(this)
            Timber.plant(Timber.DebugTree())
        }
    }
}