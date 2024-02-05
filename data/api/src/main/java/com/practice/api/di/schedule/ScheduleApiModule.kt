package com.practice.api.di.schedule

import com.practice.api.schedule.api.ScheduleApi
import com.practice.api.schedule.api.scheduleApi
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