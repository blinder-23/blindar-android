package com.example.server.schedule

import com.example.server.schedule.api.scheduleApi
import com.example.server.toEpochDate
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScheduleApiTest {

    private val api = scheduleApi

    @Test
    fun testScheduleApi(): Unit = runBlocking {
        val response = api.getSchedules(2022, 9)
        assertThat(response.schedules).isNotEmpty
            .allSatisfy {
                assertThat(it.date.toEpochDate(9)).hasYear(2022).hasMonthValue(9)
            }
    }

}