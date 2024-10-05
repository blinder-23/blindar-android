package com.practice.api.memo

import com.practice.api.memo.converter.toRemote
import com.practice.api.memo.pojo.DeleteMemoResponse
import com.practice.api.memo.pojo.GetMemoResponse
import com.practice.api.memo.pojo.UpdateMemoResponse
import com.practice.domain.Memo

class FakeRemoteMemoDataSource : RemoteMemoDataSource {
    private var id: Int = 0
    private val memos = mutableListOf<Memo>()
    override suspend fun getAllMemos(userId: String): GetMemoResponse {
        val userMemos = memos.filter { it.userId == userId }.map { it.toRemote() }
        return GetMemoResponse(userMemos)
    }

    override suspend fun updateMemo(memo: Memo): UpdateMemoResponse {
        val idGivenMemo = if (memos.removeAll { it.id == memo.id }) {
            memo
        } else {
            memo.copy(id = assignNewId())
        }
        memos.add(idGivenMemo)
        return UpdateMemoResponse(isSuccess = true, id = idGivenMemo.id)
    }

    override suspend fun deleteMemo(id: String): DeleteMemoResponse {
        val deleteTargetIndex = memos.indexOfFirst { it.id == id }
        val message = if (deleteTargetIndex != -1) {
            val target = memos[deleteTargetIndex]
            memos.removeAt(deleteTargetIndex)
            "Memo [${target.content}] is removed."
        } else {
            "Memo ID doesn't exist. No memo object is removed."
        }
        return DeleteMemoResponse(message)
    }

    private fun assignNewId(): String {
        return (++id).toString()
    }

    fun clear() {
        memos.clear()
    }
}