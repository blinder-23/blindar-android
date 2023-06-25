package com.practice.api.di.school

import com.practice.api.school.api.SchoolApi
import com.practice.api.school.api.schoolApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SchoolApiModule {
    @Singleton
    @Provides
    fun provideSchoolApi(): SchoolApi = schoolApi
}