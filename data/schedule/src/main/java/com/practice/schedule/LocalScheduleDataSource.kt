package com.practice.schedule

import com.hsk.ktx.getDateString
import com.practice.domain.schedule.Schedule
import com.practice.schedule.room.ScheduleDao
import com.practice.schedule.room.toRoomEntities
import com.practice.schedule.room.toScheduleEntities
import com.practice.schedule.room.toScheduleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalScheduleDataSource(private val scheduleDao: ScheduleDao) : ScheduleDataSource {

    override fun getSchedules(
        schoolCode: Int,
        year: Int,
        month: Int,
    ): Flow<List<Schedule>> {
        return scheduleDao.getSchedule(schoolCode, getDateString(year, month))
            .map { it.toScheduleEntities() }
    }

    override suspend fun getSchedulesPlain(schoolCode: Int, year: Int, month: Int): List<Schedule> {
        return scheduleDao.getSchedulePlain(schoolCode, getDateString(year, month))
            .map { it.toScheduleEntity() }
    }

    override suspend fun getSchedules(schoolCode: Int, ymd: String): List<Schedule> {
        return scheduleDao.getSchedules(schoolCode, ymd).map { it.toScheduleEntity() }
    }

    override suspend fun insertSchedules(schedules: List<Schedule>) {
        scheduleDao.insertSchedules(schedules.toRoomEntities())
    }

    override suspend fun deleteSchedules(schedules: List<Schedule>) {
        scheduleDao.deleteSchedules(schedules.toRoomEntities())
    }

    override suspend fun deleteSchedules(schoolCode: Int, year: Int, month: Int) {
        scheduleDao.deleteSchedules(schoolCode, getDateString(year, month))
    }

    override suspend fun clear() {
        scheduleDao.clear()
    }
}