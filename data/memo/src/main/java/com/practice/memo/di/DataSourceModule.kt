package com.practice.memo.di

import com.practice.memo.LocalMemoDataSource
import com.practice.memo.MemoDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindMemoDataSource(localMemoDataSource: LocalMemoDataSource): MemoDataSource
}