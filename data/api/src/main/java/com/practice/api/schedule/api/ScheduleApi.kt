package com.practice.api.schedule.api

import com.practice.api.BlindarRetrofit
import com.practice.api.schedule.pojo.ScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {

    @GET("schedule")
    suspend fun getSchedules(
        @Query("school_code") schoolCode: Int,
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): ScheduleResponse
}

val scheduleApi: ScheduleApi
    get() = BlindarRetrofit.getRetrofit(getScheduleConverter())
        .create(ScheduleApi::class.java)