package com.practice.api.di.user

import com.practice.api.user.RemoteUserDataSource
import com.practice.api.user.RemoteUserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteUserDataStoreModule {
    @Binds
    abstract fun bindRemoteUserDataSource(
        impl: RemoteUserDataSourceImpl
    ): RemoteUserDataSource
}