package com.practice.memo

import android.util.Log
import com.hsk.ktx.getDateString
import com.practice.domain.Memo
import com.practice.memo.room.MemoDao
import com.practice.memo.room.toEntity
import com.practice.memo.room.toMemo
import com.practice.util.date.DateUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalMemoDataSource @Inject constructor(private val memoDao: MemoDao) : MemoDataSource {
    override suspend fun getMemos(userId: String, year: Int, month: Int, day: Int): List<Memo> {
        val dateString = DateUtil.toDateString(year, month, day)
        return memoDao.getMemos(userId, dateString).map { memoEntity -> memoEntity.toMemo() }
    }

    override fun getMemos(userId: String, year: Int, month: Int): Flow<List<Memo>> {
        val monthPadZeroStart = month.toString().padStart(2, '0')
        return memoDao.getMemos(userId, year, monthPadZeroStart).map { memoEntity ->
            Log.d("LocalMemoDataSource", "memo $year $month: ${memoEntity.size}")
            memoEntity.toMemo()
        }
    }

    override suspend fun getMemosPlain(userId: String, year: Int, month: Int): List<Memo> {
        val monthPadZeroStart = month.toString().padStart(2, '0')
        return memoDao.getMemosPlain(userId, year, monthPadZeroStart)
            .map { memoEntity -> memoEntity.toMemo() }
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

    override suspend fun deleteMemo(userId: String, year: Int, month: Int, day: Int) {
        memoDao.deleteMemo(userId, getDateString(year, month, day))
    }

    override suspend fun clear() {
        memoDao.clearMemoDatabase()
    }
}