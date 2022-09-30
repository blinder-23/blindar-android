package com.example.di.schedule

import com.example.server.schedule.api.ScheduleApi
import com.example.server.schedule.api.scheduleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ScheduleApiModule {

    @Singleton
    @Provides
    fun provideScheduleApi(): ScheduleApi = scheduleApi
}