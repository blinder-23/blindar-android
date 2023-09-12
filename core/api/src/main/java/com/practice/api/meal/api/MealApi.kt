package com.practice.api.meal.api

import com.practice.api.ServerRetrofit
import com.practice.api.meal.pojo.MealResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("meal")
    suspend fun getMeals(
        @Query("school_code") schoolCode: Int,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): MealResponse
}

val mealApi: MealApi
    get() = ServerRetrofit.getRetrofit(GsonConverterFactory.create())
        .create(MealApi::class.java)