package com.practice.api.schedule

import com.practice.api.schedule.pojo.ScheduleResponse

interface RemoteScheduleDataSource {
    suspend fun getSchedules(year: Int, month: Int): ScheduleResponse
}