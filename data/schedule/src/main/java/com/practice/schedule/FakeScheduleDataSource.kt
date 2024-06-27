package com.practice.schedule

import com.practice.domain.schedule.Schedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeScheduleDataSource : ScheduleDataSource {

    private val scheduleEntities = mutableSetOf<Schedule>()

    override suspend fun getSchedules(
        schoolCode: Int,
        year: Int,
        month: Int,
    ): Flow<List<Schedule>> {
        return flow {
            emit(scheduleEntities.filter { it.schoolCode == schoolCode && it.year == year && it.month == month })
        }
    }

    override suspend fun getSchedulesPlain(schoolCode: Int, year: Int, month: Int): List<Schedule> {
        return scheduleEntities.filter { it.schoolCode == schoolCode && it.year == year && it.month == month }
    }

    override suspend fun getSchedules(schoolCode: Int, ymd: String): List<Schedule> {
        return try {
            val year = ymd.substring(0..3).toInt()
            val month = ymd.substring(4..5).toInt()
            val day = ymd.substring(6..7).toInt()
            scheduleEntities.filter {
                it.schoolCode == schoolCode && it.year == year && it.month == month && it.day == day
            }
        } catch (e: NumberFormatException) {
            emptyList()
        }
    }

    override suspend fun insertSchedules(schedules: List<Schedule>) {
        scheduleEntities.addAll(schedules)
    }

    override suspend fun deleteSchedules(schedules: List<Schedule>) {
        scheduleEntities.removeAll(schedules.toSet())
    }

    override suspend fun deleteSchedules(schoolCode: Int, year: Int, month: Int) {
        scheduleEntities.removeAll { it.schoolCode == schoolCode && it.year == year && it.month == month }
    }

    override suspend fun clear() {
        scheduleEntities.clear()
    }
}