package com.practice.neis.schedule

import com.practice.neis.schedule.pojo.ScheduleModel

class RemoteScheduleRepository(private val remoteScheduleDataSource: RemoteScheduleDataSource) {

    suspend fun getSchedules(year: Int, month: Int): List<ScheduleModel> {
        return try {
            remoteScheduleDataSource.getSchedules(year, month)
        } catch (e: RemoteScheduleDataSourceException) {
            throw RemoteScheduleRepositoryException(e.message)
        }
    }

}

class RemoteScheduleRepositoryException(override val message: String) : Exception(message)