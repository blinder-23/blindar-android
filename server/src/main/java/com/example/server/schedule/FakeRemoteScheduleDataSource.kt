package com.example.server.schedule

import com.example.server.schedule.pojo.ScheduleModel
import com.example.server.schedule.pojo.ScheduleResponse
import com.example.server.toEpochDate
import java.time.LocalDate
import java.time.ZoneOffset

class FakeRemoteScheduleDataSource : RemoteScheduleDataSource {
    private val data = (1..10).map {
        ScheduleModel(
            id = it,
            date = LocalDate.of(2022, it, 10)
                .atStartOfDay()
                .toEpochSecond(ZoneOffset.ofHours(9)),
            title = "${it}번 학사일정"
        )
    }

    override suspend fun getSchedules(year: Int, month: Int): ScheduleResponse {
        val filtered = data.filter {
            val date = it.date.toEpochDate(9)
            date.year == year && date.monthValue == month
        }
        return ScheduleResponse(filtered)
    }
}