package com.practice.api.memo

import com.practice.api.memo.api.MemoApi
import com.practice.api.memo.converter.toRemote
import com.practice.api.memo.pojo.DeleteMemoResponse
import com.practice.api.memo.pojo.GetMemoResponse
import com.practice.api.memo.pojo.UpdateMemoResponse
import com.practice.domain.Memo
import javax.inject.Inject

class RemoteMemoDataSourceImpl @Inject constructor(private val api: MemoApi) :
    RemoteMemoDataSource {
    override suspend fun getAllMemos(userId: String): GetMemoResponse {
        return api.getAllMemoOfUser(userId)
    }

    override suspend fun updateMemo(memo: Memo): UpdateMemoResponse {
        return api.updateMemo(memo.toRemote())
    }

    override suspend fun deleteMemo(id: String): DeleteMemoResponse {
        return api.deleteMemo(id)
    }
}