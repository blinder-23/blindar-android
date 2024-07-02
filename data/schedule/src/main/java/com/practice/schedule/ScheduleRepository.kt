package com.practice.schedule

import com.hsk.ktx.date.Date
import com.hsk.ktx.getDateString
import com.practice.domain.schedule.Schedule
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val dataSource: ScheduleDataSource) {

    fun getSchedules(schoolCode: Int, year: Int, month: Int): Flow<List<Schedule>> {
        return dataSource.getSchedules(schoolCode, year, month)
    }

    suspend fun getSchedulesPlain(schoolCode: Int, year: Int, month: Int): List<Schedule> {
        return dataSource.getSchedulesPlain(schoolCode, year, month)
    }

    suspend fun getSchedules(schoolCode: Int, date: Date): List<Schedule> {
        val (year, month, day) = date
        return dataSource.getSchedules(schoolCode, getDateString(year, month, day))
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