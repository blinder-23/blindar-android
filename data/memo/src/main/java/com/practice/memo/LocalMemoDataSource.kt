package com.practice.memo

import com.practice.domain.Memo
import com.practice.memo.room.MemoDao
import com.practice.memo.room.toEntity
import com.practice.memo.room.toMemo
import javax.inject.Inject

class LocalMemoDataSource @Inject constructor(private val memoDao: MemoDao) : MemoDataSource {
    override suspend fun getMemos(userId: String, year: Int, month: Int): List<Memo> {
        val monthPadZeroStart = month.toString().padStart(2, '0')
        return memoDao.getMemos(userId, year, monthPadZeroStart).map { memoEntity ->
            memoEntity.toMemo()
        }
    }

    override suspend fun insertMemo(memo: Memo) {
        memoDao.insertMemo(memo.toEntity())
    }

    override suspend fun updateMemo(memo: Memo) {
        memoDao.updateMemo(memo.toEntity())
    }

    override suspend fun deleteMemo(memo: Memo) {
        memoDao.deleteMemo(memo.toEntity())
    }

    override suspend fun deleteMemo(id: String) {
        memoDao.deleteMemo(id)
    }

    override suspend fun clear() {
        memoDao.clearMemoDatabase()
    }
}