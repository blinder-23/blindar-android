package com.example.server.schedule

import com.example.server.schedule.pojo.ScheduleResponse

interface RemoteScheduleDataSource {
    suspend fun getSchedules(year: Int, month: Int): ScheduleResponse
}