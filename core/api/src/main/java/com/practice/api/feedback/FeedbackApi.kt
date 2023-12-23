package com.practice.api.feedback

import com.practice.api.BlindarRetrofit
import com.practice.api.feedback.pojo.FeedbackRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface FeedbackApi {
    @POST("feedback")
    suspend fun sendFeedback(
        @Body body: FeedbackRequestBody,
    ): Response<ResponseBody>
}

val feedbackApi: FeedbackApi
    get() = BlindarRetrofit.getRetrofit(GsonConverterFactory.create())
        .create(FeedbackApi::class.java)