package com.example.server.meal.api

import com.example.server.ServerRetrofit
import com.example.server.meal.pojo.MealResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("meal")
    suspend fun getMeals(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): MealResponse
}

val mealApi: MealApi
    get() = ServerRetrofit.getRetrofit(GsonConverterFactory.create())
        .create(MealApi::class.java)