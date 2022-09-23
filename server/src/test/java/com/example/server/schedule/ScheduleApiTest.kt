package com.example.server.schedule

import com.example.server.schedule.api.scheduleApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class ScheduleApiTest {

    private val api = scheduleApi

    @Test
    fun testScheduleApi() = runBlocking {
        val response = api.getSchedules(2022, 9)
        println(response)
    }

}