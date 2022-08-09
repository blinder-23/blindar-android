package com.practice.neis.schedule

import com.practice.neis.schedule.pojo.ScheduleModel

class ScheduleRemoteRepository(private val scheduleRemoteDataSource: ScheduleRemoteDataSource) {

    suspend fun getSchedules(year: Int, month: Int): List<ScheduleModel> {
        return try {
            scheduleRemoteDataSource.getSchedules(year, month)
        } catch (e: ScheduleRemoteDataSourceException) {
            throw ScheduleRemoteRepositoryException(e.message)
        }
    }

}

class ScheduleRemoteRepositoryException(override val message: String) : Exception(message)