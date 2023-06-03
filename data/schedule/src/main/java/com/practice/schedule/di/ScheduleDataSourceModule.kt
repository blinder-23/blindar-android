package com.practice.schedule.di

import com.practice.schedule.LocalScheduleDataSource
import com.practice.schedule.ScheduleDataSource
import com.practice.schedule.room.ScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ScheduleDataSourceModule {

    @Provides
    fun provideLocalScheduleDataSource(
        scheduleDao: ScheduleDao
    ): ScheduleDataSource = LocalScheduleDataSource(scheduleDao)
}