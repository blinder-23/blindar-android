package com.practice.api.schedule

import com.practice.api.schedule.api.scheduleApi

class RemoteScheduleRepositoryImplTest {

    private val dataSource: RemoteScheduleDataSource = RemoteScheduleDataSourceImpl(scheduleApi)

    // TODO: 학사일정 api 고쳐지면 다시 켜기
//    @Test
//    fun `test schedules`(): Unit = runBlocking {
//        val year = 2022
//        val month = 9
//        val result = dataSource.getSchedules(7010578, year, month)
//
//        assertThat(result.schedules).isNotEmpty
//            .allSatisfy {
//                val scheduleMonth = it.date.toEpochDate(9).month
//                assertThat(scheduleMonth).isEqualTo(9)
//            }
//    }

}