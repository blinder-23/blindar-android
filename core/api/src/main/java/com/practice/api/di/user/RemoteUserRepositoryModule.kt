package com.practice.api.di.user

import com.practice.api.user.RemoteUserDataSource
import com.practice.api.user.RemoteUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteUserRepositoryModule {
    @Singleton
    @Provides
    fun provideRemoteUserRepository(
        userDataSource: RemoteUserDataSource
    ): RemoteUserRepository = RemoteUserRepository(userDataSource)
}