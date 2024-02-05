package com.practice.api.user

import com.practice.api.BlindarRetrofit
import com.practice.api.user.pojo.UserUpdateRequestBody
import com.practice.api.user.pojo.UserUpdateResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("user/update")
    suspend fun updateUser(
        @Body body: UserUpdateRequestBody,
    ): UserUpdateResponse
}

val userApi: UserApi
    get() = BlindarRetrofit.getRetrofit(GsonConverterFactory.create())
        .create(UserApi::class.java)