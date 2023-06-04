package com.practice.api.schedule

import com.hsk.ktx.date.Date
import com.practice.api.schedule.pojo.ScheduleModel
import com.practice.api.schedule.pojo.ScheduleResponse
import com.practice.api.toEpochDate

class FakeRemoteScheduleDataSource : RemoteScheduleDataSource {
    private val data = (1..10).map {
        ScheduleModel(
            id = it,
            date = Date(2022, it, 10).toEpochSecond(),
            title = "${it}번 학사일정",
            contents = "$it",
        )
    }

    override suspend fun getSchedules(year: Int, month: Int): ScheduleResponse {
        val filtered = data.filter {
            val date = it.date.toEpochDate(9)
            date.year == year && date.month == month
        }
        return ScheduleResponse(filtered)
    }
}