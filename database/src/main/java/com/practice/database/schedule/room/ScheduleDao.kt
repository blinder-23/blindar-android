package com.practice.database.schedule.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedule WHERE date LIKE :yearMonth || '%%'")
    fun getSchedule(yearMonth: String): Flow<List<ScheduleEntityRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ScheduleEntityRoom>)

    @Delete
    suspend fun deleteSchedules(schedules: List<ScheduleEntityRoom>)

    @Query("DELETE FROM schedule WHERE date LIKE :yearMonth || '%%'")
    suspend fun deleteSchedules(yearMonth: String)

    @Query("DELETE FROM schedule")
    suspend fun clear()

}