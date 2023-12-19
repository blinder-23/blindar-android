package com.practice.api.di.user

import com.practice.api.user.UserApi
import com.practice.api.user.userApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserApiModule {
    @Singleton
    @Provides
    fun provideUserApi(): UserApi = userApi
}