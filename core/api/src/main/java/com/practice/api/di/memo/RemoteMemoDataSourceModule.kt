package com.practice.api.di.memo

import com.practice.api.memo.RemoteMemoDataSource
import com.practice.api.memo.RemoteMemoDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteMemoDataSourceModule {
    @Binds
    abstract fun bindRemoteMemoDataSource(
        impl: RemoteMemoDataSourceImpl
    ): RemoteMemoDataSource
}