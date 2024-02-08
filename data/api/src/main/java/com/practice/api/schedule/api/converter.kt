package com.practice.api.schedule.api

import com.google.gson.GsonBuilder
import com.practice.api.schedule.pojo.ScheduleDeserializer
import com.practice.api.schedule.pojo.ScheduleResponse
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

fun getScheduleConverter(): Converter.Factory = GsonConverterFactory.create(
    GsonBuilder().registerTypeAdapter(
        ScheduleResponse::class.java,
        ScheduleDeserializer()
    ).create()
)
