package com.practice.api.di.school

import com.practice.api.school.RemoteSchoolDataSource
import com.practice.api.school.RemoteSchoolDataSourceImpl
import com.practice.api.school.RemoteSchoolRepository
import com.practice.api.school.api.SchoolApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteSchoolModule {
    @Singleton
    @Provides
    fun provideRemoteSchoolDataSource(
        api: SchoolApi
    ): RemoteSchoolDataSource = RemoteSchoolDataSourceImpl(api)

    @Singleton
    @Provides
    fun provideRemoteSchoolRepository(
        dataSource: RemoteSchoolDataSource
    ): RemoteSchoolRepository = RemoteSchoolRepository(dataSource)
}