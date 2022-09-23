package com.example.server.schedule.api

import com.example.server.schedule.pojo.ScheduleDeserializer
import com.example.server.schedule.pojo.ScheduleResponse
import com.google.gson.GsonBuilder
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

fun getScheduleConverter(): Converter.Factory = GsonConverterFactory.create(
    GsonBuilder().registerTypeAdapter(
        ScheduleResponse::class.java,
        ScheduleDeserializer()
    ).create()
)
