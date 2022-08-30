package com.practice.di.schedule

import com.practice.database.schedule.ScheduleDataSource
import com.practice.database.schedule.ScheduleLocalDataSource
import com.practice.database.schedule.room.ScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ScheduleDataSourceModule {

    @Provides
    fun provideLocalDataSource(
        scheduleDao: ScheduleDao
    ): ScheduleDataSource = ScheduleLocalDataSource(scheduleDao)
}