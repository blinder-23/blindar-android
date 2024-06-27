package com.practice.memo

import com.hsk.ktx.date.Date
import com.practice.domain.Memo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MemoRepository @Inject constructor(private val dataSource: MemoDataSource) {

    suspend fun getMemos(userId: String, date: Date): List<Memo> {
        val (year, month, day) = date
        return withIoContext {
            dataSource.getMemos(userId, year, month, day)
        }
    }

    suspend fun getMemos(userId: String, year: Int, month: Int): Flow<List<Memo>> {
        return withIoContext {
            dataSource.getMemos(userId, year, month)
        }
    }

    suspend fun getMemosPlain(userId: String, year: Int, month: Int): List<Memo> {
        return withIoContext { dataSource.getMemosPlain(userId, year, month) }
    }

    suspend fun insertMemo(memo: Memo) {
        withIoContext {
            dataSource.insertMemo(memo)
        }
    }

    suspend fun insertMemos(memos: List<Memo>) {
        withIoContext {
            memos.forEach { insertMemo(it) }
        }
    }

    suspend fun updateMemo(memo: Memo) {
        withIoContext {
            dataSource.updateMemo(memo)
        }
    }

    suspend fun updateMemos(memos: List<Memo>) {
        withIoContext {
            memos.forEach { updateMemo(it) }
        }
    }

    suspend fun deleteMemo(memo: Memo) {
        withIoContext {
            dataSource.deleteMemo(memo)
        }
    }

    suspend fun deleteMemo(id: String) {
        withIoContext {
            dataSource.deleteMemo(id)
        }
    }

    suspend fun deleteMemo(userId: String, date: Date) {
        val (year, month, day) = date
        withIoContext {
            dataSource.deleteMemo(userId, year, month, day)
        }
    }

    suspend fun clearMemoDatabase() {
        withIoContext {
            dataSource.clear()
        }
    }

    private suspend fun <T> withIoContext(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }

}