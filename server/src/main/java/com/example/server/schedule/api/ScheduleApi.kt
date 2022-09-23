package com.example.server.schedule.api

import com.example.server.ServerRetrofit
import com.example.server.schedule.pojo.ScheduleResponse
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