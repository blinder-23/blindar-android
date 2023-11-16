package com.practice.api.memo

import com.practice.domain.Memo
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoteMemoRepositoryTest {
    private val remoteMemoDataSource = FakeRemoteMemoDataSource()
    private val remoteMemoRepository = RemoteMemoRepository(remoteMemoDataSource)

    @BeforeEach
    fun setUp() {
        remoteMemoDataSource.clear()
    }

    @Test
    fun `DB가 처음에 완전히 비어있는지 확인`() = runTest {
        assertThat(remoteMemoRepository.getAllMemos("test"))
            .isEmpty()
    }

    @Test
    fun `새로 삽입한 메모가 잘 저장됐는지 확인`() = runTest {
        val userId = "test"
        val expectedMemos = (1..5).map {
            val memo = createNewMemo(userId)
            val assignedId = remoteMemoRepository.updateMemo(memo)
            memo.copy(id = assignedId)
        }

        val actualMemo = remoteMemoRepository.getAllMemos(userId)
        assertThat(actualMemo)
            .containsExactlyInAnyOrderElementsOf(expectedMemos)
    }

    @Test
    fun `메모가 정상적으로 수정되는지 확인`() = runTest {
        val userId = "test"
        val insertedMemos = (1..5).map {
            val memo = createNewMemo(userId)
            val assignedId = remoteMemoRepository.updateMemo(memo)
            memo.copy(id = assignedId)
        }.toMutableList()

        insertedMemos[2] = insertedMemos[2].copy(content = "some content")
        remoteMemoRepository.updateMemo(insertedMemos[2])

        val allMemos = remoteMemoRepository.getAllMemos(userId)
        assertThat(allMemos)
            .containsExactlyInAnyOrderElementsOf(insertedMemos)
    }

    @Test
    fun `메모가 정상적으로 삭제되는지 확인`() = runTest {
        val userId = "test"
        val insertedMemos = (1..5).map {
            val memo = createNewMemo(userId)
            val assignedId = remoteMemoRepository.updateMemo(memo)
            memo.copy(id = assignedId)
        }.toMutableList()

        remoteMemoRepository.deleteMemo(insertedMemos.last().id)

        val allMemos = remoteMemoRepository.getAllMemos(userId)
        assertThat(allMemos)
            .containsExactlyInAnyOrderElementsOf(insertedMemos.subList(0, insertedMemos.lastIndex))
    }

    private fun createNewMemo(userId: String) = Memo(
        id = "",
        userId = userId,
        year = 2023,
        month = 11,
        day = 12,
        content = "content",
        isSavedOnRemote = false,
    )
}