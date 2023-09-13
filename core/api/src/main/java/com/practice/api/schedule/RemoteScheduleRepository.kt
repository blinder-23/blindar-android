package com.practice.api.schedule

import com.practice.api.schedule.pojo.toMonthlySchedule
import com.practice.domain.schedule.MonthlySchedule

class RemoteScheduleRepository(private val remote: RemoteScheduleDataSource) {
    suspend fun getSchedules(schoolCode: Int, year: Int, month: Int): MonthlySchedule {
        return try {
            remote.getSchedules(schoolCode, year, month).toMonthlySchedule(schoolCode, year, month)
        } catch (e: Exception) {
            throw RemoteScheduleRepositoryException(e.message)
        }
    }
}

class RemoteScheduleRepositoryException(override val message: String?) : Exception(message)