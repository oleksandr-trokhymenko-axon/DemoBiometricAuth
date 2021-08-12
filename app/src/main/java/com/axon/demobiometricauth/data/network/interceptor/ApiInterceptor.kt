package com.axon.demobiometricauth.data.network.interceptor

import com.axon.demobiometricauth.BuildConfig
import com.axon.demobiometricauth.data.local.EncryptPrefsManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiInterceptor @Inject constructor(private val prefsManager: EncryptPrefsManager) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalUrl = chain.request().url
        val urlBuilder = originalUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)

        if (prefsManager.sessionId.isNotEmpty()) {
            urlBuilder.addQueryParameter("session_id", prefsManager.sessionId)
        }

        val requestBuilder = chain.request()
            .newBuilder()
            .url(urlBuilder.build())
            .build()

        return chain.proceed(requestBuilder)
    }
}