package com.practice.database.schedule

import com.practice.database.schedule.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val dataSource: ScheduleDataSource) {

    suspend fun getSchedules(year: Int, month: Int): Flow<List<ScheduleEntity>> {
        return dataSource.getSchedules(year, month)
    }

    suspend fun insertSchedules(schedules: List<ScheduleEntity>) {
        dataSource.insertSchedules(schedules)
    }

    suspend fun insertSchedules(vararg schedules: ScheduleEntity) {
        insertSchedules(schedules.asList())
    }

    suspend fun deleteSchedules(schedules: List<ScheduleEntity>) {
        dataSource.deleteSchedules(schedules)
    }

    suspend fun deleteSchedules(vararg schedules: ScheduleEntity) {
        deleteSchedules(schedules.asList())
    }

    suspend fun deleteSchedules(year: Int, month: Int) {
        dataSource.deleteSchedules(year, month)
    }

    suspend fun clear() {
        dataSource.clear()
    }

}