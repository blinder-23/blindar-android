package com.practice.schedule

import com.hsk.ktx.getDateString
import com.practice.schedule.entity.ScheduleEntity
import com.practice.schedule.room.ScheduleDao
import com.practice.schedule.room.toRoomEntities
import com.practice.schedule.room.toScheduleEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalScheduleDataSource(private val scheduleDao: ScheduleDao) : ScheduleDataSource {

    override suspend fun getSchedules(year: Int, month: Int): Flow<List<ScheduleEntity>> {
        return scheduleDao.getSchedule(getDateString(year, month)).map { it.toScheduleEntities() }
    }

    override suspend fun insertSchedules(schedules: List<ScheduleEntity>) {
        scheduleDao.insertSchedules(schedules.toRoomEntities())
    }

    override suspend fun deleteSchedules(schedules: List<ScheduleEntity>) {
        scheduleDao.deleteSchedules(schedules.toRoomEntities())
    }

    override suspend fun deleteSchedules(year: Int, month: Int) {
        scheduleDao.deleteSchedules(getDateString(year, month))
    }

    override suspend fun clear() {
        scheduleDao.clear()
    }
}