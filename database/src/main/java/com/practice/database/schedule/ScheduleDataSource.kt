package com.practice.database.schedule

import com.practice.database.schedule.entity.ScheduleEntity

sealed interface ScheduleDataSource {
    suspend fun getSchedules(year: Int, month: Int): List<ScheduleEntity>

    suspend fun insertSchedules(schedules: List<ScheduleEntity>)

    suspend fun deleteSchedules(schedules: List<ScheduleEntity>)

    suspend fun deleteSchedules(year: Int, month: Int)

    suspend fun clear()
}