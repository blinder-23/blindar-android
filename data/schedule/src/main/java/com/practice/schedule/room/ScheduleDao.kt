package com.practice.schedule.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedule WHERE school_code = :schoolCode AND date LIKE :yearMonth || '%%'")
    fun getSchedule(schoolCode: Int, yearMonth: String): Flow<List<ScheduleEntityRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ScheduleEntityRoom>)

    @Delete
    suspend fun deleteSchedules(schedules: List<ScheduleEntityRoom>)

    @Query("DELETE FROM schedule WHERE school_code = :schoolCode AND date LIKE :yearMonth || '%%'")
    suspend fun deleteSchedules(schoolCode: Int, yearMonth: String)

    @Query("DELETE FROM schedule")
    suspend fun clear()

}