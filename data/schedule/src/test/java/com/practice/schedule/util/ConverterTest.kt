package com.practice.schedule.util

import com.hsk.ktx.getDateString
import com.practice.schedule.entity.ScheduleEntity
import com.practice.schedule.room.ScheduleEntityRoom
import com.practice.schedule.room.toRoomEntity
import com.practice.schedule.room.toScheduleEntity
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(scheduleEntity.toRoomEntity()).isEqualTo(expected)
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
        assertThat(roomEntity.toScheduleEntity()).isEqualTo(expected)
    }
}