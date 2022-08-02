package com.example.searchbook.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

class ApiProvider {
    private val baseURL = "https://openapi.naver.com/"
    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .addHeader("X-Naver-Client-Id", "Yx4EzmZSd6ZfY2LJaY05")
            .addHeader("X-Naver-Client-Secret", "uiWY05fyTt")
            .build()
        chain.proceed(request)
    }
    private val client = OkHttpClient
        .Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    private fun Retrofit.Builder.setUrl(
        url: String
    ): Retrofit.Builder {
        return this
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
    }

    fun <T : Any> create(kClass: KClass<T>): T = Retrofit.Builder()
        .setUrl(baseURL)
        .build()
        .create(kClass.java)
}