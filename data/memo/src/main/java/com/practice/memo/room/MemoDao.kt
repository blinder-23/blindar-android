package com.practice.memo.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo WHERE user_id = :userId AND date LIKE :year || :month || '%'")
    fun getMemos(userId: String, year: Int, month: String): Flow<List<MemoEntity>>

    @Insert
    suspend fun insertMemo(memo: MemoEntity)

    @Update
    suspend fun updateMemo(memo: MemoEntity)

    @Delete
    suspend fun deleteMemo(memo: MemoEntity)

    @Query("DELETE FROM memo WHERE id = :id")
    suspend fun deleteMemo(id: String)

    @Query("DELETE FROM memo")
    suspend fun clearMemoDatabase()
}