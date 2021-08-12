package com.axon.demobiometricauth.di

import com.axon.demobiometricauth.BuildConfig
import com.axon.demobiometricauth.data.network.services.UserService
import com.axon.demobiometricauth.data.network.interceptor.ApiInterceptor
import com.google.gson.GsonBuilder
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(apiInterceptor: ApiInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(apiInterceptor)
            .setUpLogging()
            .build()
    }

    private fun OkHttpClient.Builder.setUpLogging(): OkHttpClient.Builder = apply {
        getLoggingInterceptors().forEach {
            addNetworkInterceptor(it)
        }
    }

    private fun getLoggingInterceptors(): List<Interceptor> {
        return if (BuildConfig.DEBUG) {
            listOf(
                OkHttpProfilerInterceptor(),
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
        } else {
            emptyList()
        }
    }
}