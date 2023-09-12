package com.practice.schedule

import com.practice.domain.schedule.Schedule
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val dataSource: ScheduleDataSource) {

    suspend fun getSchedules(schoolCode: Int, year: Int, month: Int): Flow<List<Schedule>> {
        return dataSource.getSchedules(schoolCode, year, month)
    }

    suspend fun insertSchedules(schedules: List<Schedule>) {
        dataSource.insertSchedules(schedules)
    }

    suspend fun insertSchedules(vararg schedules: Schedule) {
        insertSchedules(schedules.asList())
    }

    suspend fun deleteSchedules(schedules: List<Schedule>) {
        dataSource.deleteSchedules(schedules)
    }

    suspend fun deleteSchedules(vararg schedules: Schedule) {
        deleteSchedules(schedules.asList())
    }

    suspend fun deleteSchedules(schoolCode: Int, year: Int, month: Int) {
        dataSource.deleteSchedules(schoolCode, year, month)
    }

    suspend fun clear() {
        dataSource.clear()
    }

}