package com.practice.api.memo

import com.practice.api.BlindarRetrofit
import com.practice.api.memo.api.MemoApi
import com.practice.api.memo.pojo.RemoteMemoEntity
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import retrofit2.converter.gson.GsonConverterFactory

class MemoApiTest {
    private val api = BlindarRetrofit.getRetrofit(GsonConverterFactory.create())
        .create(MemoApi::class.java)

    @Test
    fun insertMemo() = runTest {
        val userId = "123"
        val memo = RemoteMemoEntity(
            id = "",
            userId = userId,
            date = "20231106",
            contents = "memo contents"
        )
        val result = api.updateMemo(memo)

        assertThat(result.id).isNotEmpty()
    }

    @Test
    fun insertThenUpdateMemo() = runTest {
        val userId = "1234"
        val memo = RemoteMemoEntity(
            id = "",
            userId = userId,
            date = "20231107",
            contents = "contents"
        )
        val id = assertDoesNotThrow {
            api.updateMemo(memo)
        }.id

        val updatedMemo = memo.copy(
            id = id,
            contents = "updated contents"
        )
        api.updateMemo(updatedMemo)

        val memoResponse = api.getAllMemoOfUser(userId)
        assertThat(memoResponse.memos)
            .isNotEmpty
            .containsOnly(updatedMemo)
    }

    @Test
    fun insertThenDeleteMemo() = runTest {
        val userId = "12345"
        val memos = (1..5).map {
            RemoteMemoEntity(
                id = "",
                userId = userId,
                date = "20231108",
                contents = "contents $it"
            )
        }
        val memoResponse = memos.map { api.updateMemo(it).id }

        api.deleteMemo(memoResponse[0])
        val memoIdsAfterDelete = api.getAllMemoOfUser(userId).memos.map { it.id }
        assertThat(memoIdsAfterDelete)
            .containsExactlyInAnyOrderElementsOf(
                memoResponse.subList(1, memoResponse.lastIndex + 1)
            )
    }

    @Test
    fun insertManyMemosThenGet() = runTest {
        val memos = (1..5).map {
            RemoteMemoEntity(
                id = "",
                userId = it.toString(),
                date = "20231108",
                contents = "contents"
            )
        }
        val memoIds = memos.map { api.updateMemo(it).id }

        val memoResponse = api.getAllMemoOfUser(memos[2].userId)
        assertThat(memoResponse.memos).containsOnly(memos[2].copy(id = memoIds[2]))
    }
}