package com.practice.memo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class MemoEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("user_id") val userId: String,
    val date: String,
    val content: String,
)