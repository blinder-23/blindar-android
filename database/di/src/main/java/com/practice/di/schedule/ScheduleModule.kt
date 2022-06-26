package com.practice.di.schedule

import com.practice.database.schedule.ScheduleLocalDataSource
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
    fun provideScheduleLocalDataSource(
        scheduleDao: ScheduleDao
    ): ScheduleLocalDataSource = ScheduleLocalDataSource(scheduleDao)

    @Provides
    fun provideScheduleRepository(
        scheduleLocalDataSource: ScheduleLocalDataSource
    ): ScheduleRepository = ScheduleRepository(scheduleLocalDataSource)

}