package com.practice.hanbitlunch.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NICERetrofit {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://open.neis.go.kr/hub/mealServiceDietInfo")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}