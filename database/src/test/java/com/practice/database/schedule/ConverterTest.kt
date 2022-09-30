package com.practice.database.schedule

import com.hsk.ktx.getDateString
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.database.schedule.room.ScheduleEntityRoom
import com.practice.database.schedule.room.toRoomEntity
import com.practice.database.schedule.room.toScheduleEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConverterTest {
    @Test
    fun scheduleEntity_toRoomEntity() {
        val scheduleEntity = ScheduleEntity(
            id = 1,
            year = 2022,
            month = 5,
            day = 1,
            eventName = "중간고사",
            eventContent = ""
        )
        val expected = with(scheduleEntity) {
            ScheduleEntityRoom(
                id = id,
                date = getDateString(year, month, day),
                eventName = eventName,
                eventContent = eventContent
            )
        }
        assertEquals(expected, scheduleEntity.toRoomEntity())
    }

    @Test
    fun scheduleEntityRoom_toEntity() {
        val roomEntity = ScheduleEntityRoom(
            id = 1,
            date = "20050105",
            eventName = "개교기념일",
            eventContent = "와!"
        )
        val expected = with(roomEntity) {
            ScheduleEntity(
                id = id,
                year = date.slice(0..3).toInt(),
                month = date.slice(4..5).toInt(),
                day = date.slice(6..7).toInt(),
                eventName = eventName,
                eventContent = eventContent
            )
        }
        assertEquals(expected, roomEntity.toScheduleEntity())
    }
}