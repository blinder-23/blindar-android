package com.practice.api

import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TestRetrofit {
    // TODO: 향후 테스트 서버 생기면, API 테스트 코드에 있는 retrofit을 전부 이걸로 바꾸기
    fun <T> getRetrofit(
        clazz: Class<T>,
        factory: Converter.Factory = GsonConverterFactory.create(),
    ): T = getTestRetrofit(factory).create(clazz)

    private fun getTestRetrofit(factory: Converter.Factory) = Retrofit.Builder()
        .baseUrl(TEST_SERVER_URL)
        .addConverterFactory(factory)
        .build()

    private const val TEST_SERVER_URL = "http://192.168.0.10"
}