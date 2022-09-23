package com.example.server.schedule

import com.example.server.schedule.api.ScheduleApi
import com.example.server.schedule.pojo.ScheduleResponse

class RemoteScheduleDataSourceImpl(private val api: ScheduleApi) : RemoteScheduleDataSource {
    override suspend fun getSchedules(year: Int, month: Int): ScheduleResponse {
        return api.getSchedules(year, month)
    }
}