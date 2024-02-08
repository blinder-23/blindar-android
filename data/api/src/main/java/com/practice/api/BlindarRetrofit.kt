package com.practice.api

import retrofit2.Converter
import retrofit2.Retrofit

object BlindarRetrofit {
    fun getRetrofit(factory: Converter.Factory): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL)
        .addConverterFactory(factory)
        .build()

}