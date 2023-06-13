package com.practice.api.di.school

import com.practice.api.school.FakeRemoteSchoolDataSource
import com.practice.api.school.RemoteSchoolDataSource
import com.practice.api.school.RemoteSchoolRepository
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
    fun provideRemoteSchoolDataSource(): RemoteSchoolDataSource = FakeRemoteSchoolDataSource()

    @Singleton
    @Provides
    fun provideRemoteSchoolRepository(
        dataSource: RemoteSchoolDataSource
    ): RemoteSchoolRepository = RemoteSchoolRepository(dataSource)
}