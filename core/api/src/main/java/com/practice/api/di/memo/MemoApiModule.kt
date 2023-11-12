package com.practice.api.di.memo

import com.practice.api.memo.api.MemoApi
import com.practice.api.memo.api.memoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MemoApiModule {

    @Singleton
    @Provides
    fun provideMemoApi(): MemoApi = memoApi
}