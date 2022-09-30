package com.practice.database.schedule

import com.practice.database.schedule.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeScheduleDataSource : ScheduleDataSource {

    private val scheduleEntities = mutableSetOf<ScheduleEntity>()

    override suspend fun getSchedules(year: Int, month: Int): Flow<List<ScheduleEntity>> {
        return flow {
            emit(scheduleEntities.filter { it.year == year && it.month == month })
        }
    }

    override suspend fun insertSchedules(schedules: List<ScheduleEntity>) {
        scheduleEntities.addAll(schedules)
    }

    override suspend fun deleteSchedules(schedules: List<ScheduleEntity>) {
        scheduleEntities.removeAll(schedules.toSet())
    }

    override suspend fun deleteSchedules(year: Int, month: Int) {
        scheduleEntities.removeAll { it.year == year && it.month == month }
    }

    override suspend fun clear() {
        scheduleEntities.clear()
    }
}