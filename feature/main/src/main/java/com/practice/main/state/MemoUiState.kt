package com.practice.main.state

import com.hsk.ktx.date.Date
import com.practice.domain.Memo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MemoUiState(
    val year: Int,
    val month: Int,
    val day: Int,
    val memos: ImmutableList<UiMemo>,
) {
    companion object {
        val EmptyMemoUiState = MemoUiState(
            year = Date.now().year,
            month = Date.now().month,
            day = Date.now().dayOfMonth,
            memos = persistentListOf(),
        )
    }
}

data class UiMemo(
    val id: String,
    val userId: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val contents: String,
)

fun Memo.toUiMemo() = UiMemo(
    id = id,
    userId = userId,
    year = year,
    month = month,
    day = day,
    contents = content,
)