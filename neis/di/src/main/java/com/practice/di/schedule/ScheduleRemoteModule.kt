package com.practice.di.schedule

import com.practice.neis.schedule.ScheduleRemoteDataSource
import com.practice.neis.schedule.ScheduleRemoteRepository
import com.practice.neis.schedule.retrofit.ScheduleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ScheduleRemoteModule {

    @Provides
    fun provideScheduleRemoteDataSource(
        api: ScheduleApi
    ): ScheduleRemoteDataSource = ScheduleRemoteDataSource(api)

    @Provides
    fun provideScheduleRemoteRepository(
        scheduleRemoteDataSource: ScheduleRemoteDataSource
    ): ScheduleRemoteRepository = ScheduleRemoteRepository(scheduleRemoteDataSource)
}