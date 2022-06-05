package com.practice.neis.schedule

import com.practice.neis.schedule.pojo.ScheduleModel

class ScheduleRepository(private val scheduleDataSource: ScheduleDataSource = ScheduleDataSource()) {

    suspend fun getSchedules(year: Int, month: Int): List<ScheduleModel> {
        return try {
            scheduleDataSource.getSchedule(year, month)
        } catch (e: ScheduleDataSourceException) {
            throw ScheduleRepositoryException(e.message)
        }
    }

}

class ScheduleRepositoryException(override val message: String) : Exception(message)