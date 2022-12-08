package com.practice.database.di.schedule

import com.practice.database.schedule.LocalScheduleDataSource
import com.practice.database.schedule.ScheduleRepository
import com.practice.database.schedule.room.ScheduleDao
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