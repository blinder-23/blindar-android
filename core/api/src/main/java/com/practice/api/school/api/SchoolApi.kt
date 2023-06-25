package com.practice.api.school.api

import com.practice.api.ServerRetrofit
import com.practice.api.school.pojo.SchoolsResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface SchoolApi {
    @GET("school_list")
    suspend fun getSupportedSchools(): SchoolsResponse
}

val schoolApi: SchoolApi
    get() = ServerRetrofit.getRetrofit(GsonConverterFactory.create())
        .create(SchoolApi::class.java)