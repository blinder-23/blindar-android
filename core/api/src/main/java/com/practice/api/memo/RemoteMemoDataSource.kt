package com.practice.api.memo

import com.practice.api.memo.pojo.DeleteMemoResponse
import com.practice.api.memo.pojo.GetMemoResponse
import com.practice.api.memo.pojo.UpdateMemoResponse
import com.practice.domain.Memo

interface RemoteMemoDataSource {
    suspend fun getAllMemos(userId: String): GetMemoResponse
    suspend fun updateMemo(memo: Memo): UpdateMemoResponse
    suspend fun deleteMemo(id: String): DeleteMemoResponse
}