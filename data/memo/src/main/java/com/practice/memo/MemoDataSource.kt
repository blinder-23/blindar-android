package com.practice.memo

import com.practice.domain.Memo
import kotlinx.coroutines.flow.Flow

interface MemoDataSource {
    suspend fun getMemos(userId: String, year: Int, month: Int, day: Int): List<Memo>
    suspend fun getMemos(userId: String, year: Int, month: Int): Flow<List<Memo>>

    suspend fun insertMemo(memo: Memo)

    suspend fun updateMemo(memo: Memo)

    suspend fun deleteMemo(memo: Memo)

    suspend fun deleteMemo(id: String)

    suspend fun deleteMemo(userId: String, year: Int, month: Int, day: Int)

    suspend fun clear()
}