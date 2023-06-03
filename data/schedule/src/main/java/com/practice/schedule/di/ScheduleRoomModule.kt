package com.practice.schedule.di

import android.content.Context
import androidx.room.Room
import com.practice.schedule.room.ScheduleDao
import com.practice.schedule.room.ScheduleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScheduleRoomModule {

    @Singleton
    @Provides
    fun provideScheduleDatabase(
        @ApplicationContext context: Context
    ): ScheduleDatabase =
        Room.databaseBuilder(
            context,
            ScheduleDatabase::class.java,
            "schedule-database"
        ).build()

    @Provides
    fun provideScheduleDao(
        scheduleDatabase: ScheduleDatabase
    ): ScheduleDao = scheduleDatabase.scheduleDao()
}