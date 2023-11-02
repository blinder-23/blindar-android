package com.practice.memo

import com.practice.domain.Memo

interface MemoDataSource {
    suspend fun getMemos(userId: String, year: Int, month: Int): List<Memo>

    suspend fun insertMemo(memo: Memo)

    suspend fun updateMemo(memo: Memo)

    suspend fun deleteMemo(memo: Memo)

    suspend fun deleteMemo(id: String)

    suspend fun clear()
}