package com.practice.api.schedule.api

import com.practice.api.ServerRetrofit
import com.practice.api.schedule.pojo.ScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {

    @GET("schedule")
    suspend fun getSchedules(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): ScheduleResponse
}

val scheduleApi: ScheduleApi
    get() = ServerRetrofit.getRetrofit(getScheduleConverter())
        .create(ScheduleApi::class.java)