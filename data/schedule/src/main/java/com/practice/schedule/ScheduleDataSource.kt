package com.practice.schedule

import com.practice.schedule.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

sealed interface ScheduleDataSource {
    suspend fun getSchedules(year: Int, month: Int): Flow<List<ScheduleEntity>>

    suspend fun insertSchedules(schedules: List<ScheduleEntity>)

    suspend fun deleteSchedules(schedules: List<ScheduleEntity>)

    suspend fun deleteSchedules(year: Int, month: Int)

    suspend fun clear()
}