package com.practice.di.schedule

import com.practice.neis.common.NEISRetrofit
import com.practice.neis.schedule.retrofit.ScheduleInfo
import com.practice.neis.schedule.retrofit.scheduleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ScheduleApiModule {

    @Provides
    fun provideScheduleApi(): ScheduleInfo = NEISRetrofit.scheduleApi
}