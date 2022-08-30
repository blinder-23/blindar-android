package com.practice.neis.schedule

import com.practice.neis.schedule.pojo.ScheduleModel

sealed interface ScheduleRemoteDataSource {
    suspend fun getSchedules(year: Int, month: Int): List<ScheduleModel>
}