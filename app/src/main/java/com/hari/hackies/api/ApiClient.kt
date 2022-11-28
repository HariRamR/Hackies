package com.hari.hackies.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {

        private var retrofit: Retrofit? = null

        fun getClient(): ApiInterface? {
            if (retrofit == null) {

                val log = HttpLoggingInterceptor()
                log.level = HttpLoggingInterceptor.Level.BODY

                val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
                    .addInterceptor(log)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()
                retrofit = Retrofit.Builder().client(okHttpClient)
                    .baseUrl("https://hacker-news.firebaseio.com/v0/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiInterface::class.java)
        }
    }
}