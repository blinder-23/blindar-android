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
        val response = api.getSchedules(2022, 9)
        assertThat(response.schedules)
            .isNotEmpty
            .allSatisfy {
                assertThat(it.date.toEpochDate(9)).matches { date ->
                    date.year == 2022 && date.month == 9
                }
            }
    }

}