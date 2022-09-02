package com.practice.database.schedule.room

import androidx.room.*

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedule WHERE date LIKE :yearMonth || '%%'")
    suspend fun getSchedule(yearMonth: String): List<ScheduleEntityRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ScheduleEntityRoom>)

    @Delete
    suspend fun deleteSchedules(schedules: List<ScheduleEntityRoom>)

    @Query("DELETE FROM schedule WHERE date LIKE :yearMonth || '%%'")
    suspend fun deleteSchedules(yearMonth: String)

    @Query("DELETE FROM schedule")
    suspend fun clear()

}