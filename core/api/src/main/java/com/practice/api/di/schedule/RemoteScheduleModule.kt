package com.practice.api.di.schedule

import com.practice.api.schedule.RemoteScheduleDataSource
import com.practice.api.schedule.RemoteScheduleDataSourceImpl
import com.practice.api.schedule.RemoteScheduleRepository
import com.practice.api.schedule.api.ScheduleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteScheduleModule {

    @Singleton
    @Provides
    fun provideRemoteScheduleDataSource(
        api: ScheduleApi
    ): RemoteScheduleDataSource = RemoteScheduleDataSourceImpl(api)

    @Singleton
    @Provides
    fun provideRemoteScheduleRepository(
        remoteScheduleDataSource: RemoteScheduleDataSource
    ): RemoteScheduleRepository = RemoteScheduleRepository(remoteScheduleDataSource)
}