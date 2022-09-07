package com.practice.di.schedule

import com.practice.neis.schedule.RemoteScheduleDataSource
import com.practice.neis.schedule.RemoteScheduleDataSourceImpl
import com.practice.neis.schedule.RemoteScheduleRepository
import com.practice.neis.schedule.retrofit.ScheduleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteScheduleModule {

    @Provides
    fun provideRemoteScheduleDataSourceImpl(
        api: ScheduleApi
    ): RemoteScheduleDataSource = RemoteScheduleDataSourceImpl(api)

    @Provides
    fun provideRemoteScheduleRepository(
        remoteScheduleDataSource: RemoteScheduleDataSource
    ): RemoteScheduleRepository = RemoteScheduleRepository(remoteScheduleDataSource)
}