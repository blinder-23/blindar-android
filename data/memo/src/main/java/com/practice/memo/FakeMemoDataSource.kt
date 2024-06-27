package com.practice.memo

import com.practice.domain.Memo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMemoDataSource : MemoDataSource {

    private val memos = mutableListOf<Memo>()

    override suspend fun getMemos(userId: String, year: Int, month: Int, day: Int): List<Memo> {
        return memos.filter {
            it.userId == userId && it.year == year && it.month == month && it.day == day
        }
    }

    override suspend fun getMemos(userId: String, year: Int, month: Int): Flow<List<Memo>> {
        return flow {
            emit(memos.filter { it.userId == userId && it.year == year && it.month == month })
        }
    }

    override suspend fun getMemosPlain(userId: String, year: Int, month: Int): List<Memo> {
        return memos.filter {
            it.userId == userId && it.year == year && it.month == month
        }
    }

    override suspend fun insertMemo(memo: Memo) {
        memos.add(memo)
    }

    override suspend fun updateMemo(memo: Memo) {
        val targetIndex = memos.indexOfFirst { it.id == memo.id }
        if (targetIndex != -1) {
            memos.removeAt(targetIndex)
        }
        insertMemo(memo)
    }

    override suspend fun deleteMemo(memo: Memo) {
        deleteMemo(memo.id)
    }

    override suspend fun deleteMemo(id: String) {
        val targetIndex = memos.indexOfFirst { it.id == id }
        if (targetIndex != -1) {
            memos.removeAt(targetIndex)
        }
    }

    override suspend fun deleteMemo(userId: String, year: Int, month: Int, day: Int) {
        memos.removeAll { it.userId == userId && it.year == year && it.month == month && it.day == day }
    }

    override suspend fun clear() {
        memos.clear()
    }

    private fun log(log: String) {
        println(log)
    }
}