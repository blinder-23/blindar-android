package com.practice.api.memo

import com.practice.api.memo.converter.toMemo
import com.practice.domain.Memo
import javax.inject.Inject

class RemoteMemoRepository @Inject constructor(private val remoteMemoDataSource: RemoteMemoDataSource) {

    suspend fun getAllMemos(userId: String): List<Memo> {
        return tryBlock {
            remoteMemoDataSource.getAllMemos(userId).memos.map { it.toMemo() }
        }
    }

    suspend fun updateMemo(memo: Memo): String {
        return tryBlock {
            remoteMemoDataSource.updateMemo(memo).response.id
        }
    }

    suspend fun deleteMemo(memoId: String): String {
        return tryBlock {
            remoteMemoDataSource.deleteMemo(memoId)
        }.message
    }

    private suspend fun <T> tryBlock(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: Exception) {
            throw RemoteMemoRepositoryException(e.message)
        }
    }
}