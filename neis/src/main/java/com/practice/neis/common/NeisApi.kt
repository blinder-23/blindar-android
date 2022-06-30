package com.practice.neis.common

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NeisApi {

    inline fun <reified T : Any> getRetrofit(
        baseUrl: String = "https://open.neis.go.kr/hub/",
        responseType: Class<T>,
        typeAdapter: JsonDeserializer<T>
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(createGsonConverter(responseType, typeAdapter))
        .build()

    inline fun <reified T : Any> createGsonConverter(
        type: Class<T>,
        typeAdapter: JsonDeserializer<T>
    ): Converter.Factory {
        val gsonBuilder = GsonBuilder()
            .registerTypeAdapter(type, typeAdapter)
            .create()
        return GsonConverterFactory.create(gsonBuilder)
    }
}