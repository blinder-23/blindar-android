package com.practice.memo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.memo.room.MemoDao
import com.practice.memo.room.MemoDatabase
import com.practice.memo.room.MemoEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MemoDaoTest {
    private lateinit var database: MemoDatabase
    private lateinit var dao: MemoDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MemoDatabase::class.java).build()
        dao = database.memoDao()
    }

    @After
    fun tearDown() = runTest {
        database.close()
    }

    @Test
    fun insertAndGetMemo() = runTest {
        val memoEntity = MemoEntity(
            id = "1",
            userId = "123",
            date = "20230908",
            content = "memo",
            isSavedOnRemote = false,
        )
        dao.insertMemo(memoEntity)

        val memos = dao.getMemos(
            userId = memoEntity.userId,
            year = memoEntity.date.substring(0..3).toInt(),
            month = memoEntity.date.substring(4..5),
        ).first()
        assertThat(memos).containsOnly(memoEntity)
    }

    @Test
    fun insertAndUpdateMemo() = runTest {
        val memoEntity = MemoEntity(
            id = "1",
            userId = "123",
            date = "20230908",
            content = "memo",
            isSavedOnRemote = false,
        )
        dao.insertMemo(memoEntity)

        val updatedMemoEntity = memoEntity.copy(
            content = "updated memo"
        )
        dao.updateMemo(updatedMemoEntity)

        val memos = dao.getMemos(
            userId = memoEntity.userId,
            year = memoEntity.date.substring(0..3).toInt(),
            month = memoEntity.date.substring(4..5),
        ).first()
        assertThat(memos).containsOnly(updatedMemoEntity)
    }

    @Test
    fun insertAndDeleteMemoById() = runTest {
        val memoEntity = MemoEntity(
            id = "1",
            userId = "123",
            date = "20230908",
            content = "memo",
            isSavedOnRemote = false,
        )
        dao.insertMemo(memoEntity)

        dao.deleteMemo(memoEntity.id)

        val memos = dao.getMemos(
            userId = memoEntity.userId,
            year = memoEntity.date.substring(0..3).toInt(),
            month = memoEntity.date.substring(4..5),
        ).first()
        assertThat(memos).isEmpty()
    }

    @Test
    fun insertAndDeleteMemoByObject() = runTest {
        val memoEntity = MemoEntity(
            id = "1",
            userId = "123",
            date = "20230908",
            content = "memo",
            isSavedOnRemote = false,
        )
        dao.insertMemo(memoEntity)

        dao.deleteMemo(memoEntity)

        val memos = dao.getMemos(
            userId = memoEntity.userId,
            year = memoEntity.date.substring(0..3).toInt(),
            month = memoEntity.date.substring(4..5),
        ).first()
        assertThat(memos).isEmpty()
    }

    @Test
    fun insertAndClearMemos() = runTest {
        val memos = (0..5).map {
            MemoEntity(
                id = it.toString(),
                userId = "123",
                date = "20230908",
                content = "memo $it",
                isSavedOnRemote = false,
            )
        }
        memos.forEach { dao.insertMemo(it) }

        dao.clearMemoDatabase()
        val actualMemos = dao.getMemos(
            userId = memos[0].userId,
            year = memos[0].date.substring(0..3).toInt(),
            month = memos[0].date.substring(4..5),
        ).first()
        assertThat(actualMemos).isEmpty()
    }
}