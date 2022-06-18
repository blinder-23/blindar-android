package com.practice.di

import com.practice.neis.schedule.ScheduleRemoteDataSource
import com.practice.neis.schedule.ScheduleRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ScheduleRemoteModule {

    @Provides
    fun provideScheduleRemoteRepository(
        scheduleRemoteDataSource: ScheduleRemoteDataSource
    ): ScheduleRemoteRepository = ScheduleRemoteRepository(scheduleRemoteDataSource)
}