package com.practice.memo

import com.practice.domain.Memo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MemoRepository @Inject constructor(private val dataSource: MemoDataSource) {

    suspend fun getMemos(userId: String, year: Int, month: Int): Flow<List<Memo>> {
        return dataSource.getMemos(userId, year, month)
    }

    suspend fun insertMemo(memo: Memo) {
        dataSource.insertMemo(memo)
    }

    suspend fun insertMemos(memos: List<Memo>) {
        memos.forEach { insertMemo(it) }
    }

    suspend fun updateMemo(memo: Memo) {
        dataSource.updateMemo(memo)
    }

    suspend fun updateMemos(memos: List<Memo>) {
        memos.forEach {updateMemo(it)}
    }

    suspend fun deleteMemo(memo: Memo) {
        dataSource.deleteMemo(memo)
    }

    suspend fun deleteMemo(id: String) {
        dataSource.deleteMemo(id)
    }

    suspend fun clearMemoDatabase() {
        dataSource.clear()
    }

}