package com.practice.schedule

import com.practice.domain.schedule.Schedule
import kotlinx.coroutines.flow.Flow

sealed interface ScheduleDataSource {
    fun getSchedules(schoolCode: Int, year: Int, month: Int): Flow<List<Schedule>>
    suspend fun getSchedulesPlain(schoolCode: Int, year: Int, month: Int): List<Schedule>

    suspend fun getSchedules(schoolCode: Int, ymd: String): List<Schedule>

    suspend fun insertSchedules(schedules: List<Schedule>)

    suspend fun deleteSchedules(schedules: List<Schedule>)

    suspend fun deleteSchedules(schoolCode: Int, year: Int, month: Int)

    suspend fun clear()
}