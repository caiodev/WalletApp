package br.com.caiodev.walletapp.utils.factory

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class RetrofitService {

    val baseUrl = "https://bank-app-test.herokuapp.com/api/"
    private val timberTag = "OkHttpLogging"

    inline fun <reified T> createService() =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createAndDeliverHttpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(T::class.java) as T

    fun createAndDeliverHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                    Timber.tag(timberTag).d(message)
                }).setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .hostnameVerifier { _, _ -> true }
            .build() as OkHttpClient
}