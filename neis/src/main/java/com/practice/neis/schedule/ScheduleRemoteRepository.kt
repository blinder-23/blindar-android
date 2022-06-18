package com.practice.neis.schedule

import com.practice.neis.schedule.pojo.ScheduleModel

class ScheduleRemoteRepository(private val scheduleRemoteDataSource: ScheduleRemoteDataSource = ScheduleRemoteDataSource()) {

    suspend fun getSchedules(year: Int, month: Int): List<ScheduleModel> {
        return try {
            scheduleRemoteDataSource.getSchedule(year, month)
        } catch (e: ScheduleRemoteDataSourceException) {
            throw ScheduleRemoteRepositoryException(e.message)
        }
    }

}

class ScheduleRemoteRepositoryException(override val message: String) : Exception(message)