package com.practice.api.schedule

import com.practice.api.schedule.api.scheduleApi

class ScheduleApiTest {

    private val api = scheduleApi

    // TODO: 학사일정 api 고쳐지면 다시 켜기
//    @Test
//    fun testScheduleApi(): Unit = runBlocking {
//        val response = api.getSchedules(2022, 9)
//        assertThat(response.schedules)
//            .isNotEmpty
//            .allSatisfy {
//                assertThat(it.date.toEpochDate(9)).matches { date ->
//                    date.year == 2022 && date.month == 9
//                }
//            }
//    }

}