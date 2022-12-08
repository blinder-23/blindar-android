package com.example.server.di.schedule

import com.example.server.schedule.RemoteScheduleDataSource
import com.example.server.schedule.RemoteScheduleDataSourceImpl
import com.example.server.schedule.RemoteScheduleRepository
import com.example.server.schedule.api.ScheduleApi
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