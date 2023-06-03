package com.practice.schedule.util

import com.practice.schedule.entity.ScheduleEntity
import com.practice.schedule.room.ScheduleEntityRoom

object TestUtil {
    fun createScheduleEntityRoom(count: Int): List<ScheduleEntityRoom> = (0 until count).map {
        ScheduleEntityRoom(
            id = it,
            date = (20220601 + it).toString(),
            eventName = "event $it",
            eventContent = "content $it",
        )
    }

    fun createScheduleEntity(count: Int): List<ScheduleEntity> = (0 until count).map {
        ScheduleEntity(
            id = it,
            year = 2022,
            month = 6,
            day = 1 + it,
            eventName = "event $it",
            eventContent = "content $it"
        )
    }
}