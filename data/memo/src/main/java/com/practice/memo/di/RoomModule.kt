package com.practice.memo.di

import android.content.Context
import androidx.room.Room
import com.practice.memo.room.MemoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    fun provideMemoDatabase(@ApplicationContext context: Context): MemoDatabase =
        Room.databaseBuilder(context, MemoDatabase::class.java, "memo-database")
            .build()

    @Provides
    fun provideMemoDao(memoDatabase: MemoDatabase) = memoDatabase.memoDao()
}