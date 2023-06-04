package com.practice.schedule.di

import com.practice.schedule.LocalScheduleDataSource
import com.practice.schedule.ScheduleRepository
import com.practice.schedule.room.ScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ScheduleModule {

    @Provides
    fun provideLocalScheduleDataSource(
        scheduleDao: ScheduleDao
    ): LocalScheduleDataSource = LocalScheduleDataSource(scheduleDao)

    @Provides
    fun provideScheduleRepository(
        localScheduleDataSource: LocalScheduleDataSource
    ): ScheduleRepository = ScheduleRepository(localScheduleDataSource)

}