package com.practice.database.schedule.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hsk.ktx.getDateString
import com.practice.database.schedule.entity.ScheduleEntity

@Entity(tableName = "schedule")
data class ScheduleEntityRoom(
    @PrimaryKey val date: String,
    @ColumnInfo(name = "school_code") val schoolCode: Int,
    @ColumnInfo(name = "event_name") val eventName: String,
    @ColumnInfo(name = "event_content") val eventContent: String,
)

fun ScheduleEntity.toRoomEntity() = ScheduleEntityRoom(
    date = getDateString(year, month, day),
    schoolCode = schoolCode,
    eventName = eventName,
    eventContent = eventContent
)

fun ScheduleEntityRoom.toScheduleEntity() = ScheduleEntity(
    schoolCode = schoolCode,
    year = date.slice(0..3).toInt(),
    month = date.slice(4..5).toInt(),
    day = date.slice(6..7).toInt(),
    eventName = eventName,
    eventContent = eventContent
)

fun List<ScheduleEntity>.toRoomEntities() = map { it.toRoomEntity() }

fun List<ScheduleEntityRoom>.toScheduleEntities() = map { it.toScheduleEntity() }