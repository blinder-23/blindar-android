package com.practice.schedule.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScheduleEntityRoom::class], version = 1)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}