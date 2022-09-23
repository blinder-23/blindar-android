package com.example.server.schedule

import com.example.server.schedule.pojo.ScheduleResponse

class RemoteScheduleRepository(private val remote: RemoteScheduleDataSource) {
    suspend fun getSchedules(year: Int, month: Int): ScheduleResponse {
        return try {
            remote.getSchedules(year, month)
        } catch (e: Exception) {
            throw RemoteScheduleRepositoryException(e.message)
        }
    }
}

class RemoteScheduleRepositoryException(override val message: String?) : Exception(message)