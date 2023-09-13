package com.practice.schedule.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.hsk.ktx.getDateString
import com.practice.domain.schedule.Schedule

@Entity(tableName = "schedule", primaryKeys = ["school_code", "id"])
data class ScheduleEntityRoom(
    @ColumnInfo("school_code") val schoolCode: Int,
    val id: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "event_name") val eventName: String,
    @ColumnInfo(name = "event_content") val eventContent: String,
)

val ScheduleEntityRoom.yearMonth: String
    get() = date.substring(0..5)

fun Schedule.toRoomEntity() = ScheduleEntityRoom(
    schoolCode = schoolCode,
    id = id,
    date = getDateString(year, month, day),
    eventName = eventName,
    eventContent = eventContent
)

fun ScheduleEntityRoom.toScheduleEntity() = Schedule(
    schoolCode = schoolCode,
    id = id,
    year = date.slice(0..3).toInt(),
    month = date.slice(4..5).toInt(),
    day = date.slice(6..7).toInt(),
    eventName = eventName,
    eventContent = eventContent
)

fun List<Schedule>.toRoomEntities() = map { it.toRoomEntity() }

fun List<ScheduleEntityRoom>.toScheduleEntities() = map { it.toScheduleEntity() }