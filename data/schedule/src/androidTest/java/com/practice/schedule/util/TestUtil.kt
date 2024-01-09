package com.practice.schedule.util

import com.practice.domain.schedule.Schedule
import com.practice.schedule.room.ScheduleEntityRoom

object TestUtil {
    val schoolCode = 500
    fun createScheduleEntityRoom(count: Int): List<ScheduleEntityRoom> = (0 until count).map {
        ScheduleEntityRoom(
            schoolCode = schoolCode,
            id = it.toLong(),
            date = (20220601 + it).toString(),
            eventName = "event $it",
            eventContent = "content $it",
        )
    }

    fun createScheduleEntity(count: Int): List<Schedule> = (0 until count).map {
        Schedule(
            schoolCode = schoolCode,
            id = it.toLong(),
            year = 2022,
            month = 6,
            day = 1 + it,
            eventName = "event $it",
            eventContent = "content $it"
        )
    }
}