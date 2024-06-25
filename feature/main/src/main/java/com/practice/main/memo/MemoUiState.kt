package com.practice.main.memo

import com.hsk.ktx.date.Date
import com.practice.main.state.UiMemo
import com.practice.main.state.UiSchedule

sealed interface MemoUiState {
    val date: Date
    val userId: String
    val schoolCode: Int

    data class Loading(
        override val date: Date,
        override val userId: String,
        override val schoolCode: Int,
    ) : MemoUiState

    data class Fail(
        override val date: Date,
        override val userId: String,
        override val schoolCode: Int,
    ) : MemoUiState

    data class Success(
        override val date: Date,
        override val userId: String,
        override val schoolCode: Int,
        val uiSchedules: List<UiSchedule>,
        val uiMemos: List<UiMemo>,
    ) : MemoUiState
}