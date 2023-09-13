package com.practice.schedule

import com.practice.domain.schedule.Schedule
import kotlinx.coroutines.flow.Flow

sealed interface ScheduleDataSource {
    suspend fun getSchedules(schoolCode: Int, year: Int, month: Int): Flow<List<Schedule>>

    suspend fun insertSchedules(schedules: List<Schedule>)

    suspend fun deleteSchedules(schedules: List<Schedule>)

    suspend fun deleteSchedules(schoolCode: Int, year: Int, month: Int)

    suspend fun clear()
}