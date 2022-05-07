package com.practice.hanbitlunch.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.practice.hanbitlunch.api.meal.MealDeserializer
import com.practice.hanbitlunch.api.meal.pojo.MealResponse
import com.practice.hanbitlunch.api.meal.MealServiceDietInfo
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NEISRetrofit {

    private inline fun <reified T : Any> getRetrofit(
        baseUrl: String,
        responseType: Class<T>,
        typeAdapter: JsonDeserializer<T>
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(createGsonConverter(responseType, typeAdapter))
        .build()

    private fun getRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private inline fun <reified T : Any> createGsonConverter(
        type: Class<T>,
        typeAdapter: JsonDeserializer<T>
    ): Converter.Factory {
        val gsonBuilder = GsonBuilder()
            .registerTypeAdapter(type, typeAdapter)
            .create()
        return GsonConverterFactory.create(gsonBuilder)
    }

    val mealApi: MealServiceDietInfo = getRetrofit(
        baseUrl = "https://open.neis.go.kr/hub/",
        responseType = MealResponse::class.java,
        typeAdapter = MealDeserializer()
    ).create(MealServiceDietInfo::class.java)
}