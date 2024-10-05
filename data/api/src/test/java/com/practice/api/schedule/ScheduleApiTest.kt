package com.practice.api.schedule

import com.practice.api.schedule.api.scheduleApi
import com.practice.api.toEpochDate
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScheduleApiTest {

    private val api = scheduleApi

    @Test
    fun testScheduleApi(): Unit = runBlocking {
        val year = 2024
        val month = 8
        val response = api.getSchedules(7010578, year, month)
        assertThat(response.schedules)
            .isNotEmpty
            .allSatisfy {
                assertThat(it.date.toEpochDate(9)).matches { date ->
                    date.year == year && date.month == month
                }
            }
    }

}