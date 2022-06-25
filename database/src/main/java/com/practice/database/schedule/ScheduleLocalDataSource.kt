package com.practice.database.schedule

import com.hsk.ktx.getDateString
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.database.schedule.room.ScheduleDao
import com.practice.database.schedule.room.toRoomEntities
import com.practice.database.schedule.room.toScheduleEntities

class ScheduleLocalDataSource(private val scheduleDao: ScheduleDao) {

    suspend fun getSchedules(year: Int, month: Int): List<ScheduleEntity> {
        return scheduleDao.getSchedule(getDateString(year, month)).toScheduleEntities()
    }

    suspend fun insertSchedules(schedules: List<ScheduleEntity>) {
        scheduleDao.insertSchedules(schedules.toRoomEntities())
    }

    suspend fun deleteSchedules(schedules: List<ScheduleEntity>) {
        scheduleDao.deleteSchedules(schedules.toRoomEntities())
    }

    suspend fun deleteSchedules(year: Int, month: Int) {
        scheduleDao.deleteSchedules(getDateString(year, month))
    }

    suspend fun clear() {
        scheduleDao.clear()
    }
}