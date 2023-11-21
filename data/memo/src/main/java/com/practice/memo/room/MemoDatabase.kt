package com.practice.memo.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MemoEntity::class],
    version = 1,
)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
}