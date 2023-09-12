package com.practice.api.schedule

import com.practice.api.schedule.api.ScheduleApi
import com.practice.api.schedule.pojo.ScheduleResponse

class RemoteScheduleDataSourceImpl(private val api: ScheduleApi) : RemoteScheduleDataSource {
    override suspend fun getSchedules(schoolCode: Int, year: Int, month: Int): ScheduleResponse {
        return api.getSchedules(year, month)
    }
}