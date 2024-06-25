package com.practice.main.memo

import com.practice.main.state.UiMemo

sealed interface MemoBottomSheetState {
    val uiMemo: UiMemo

    fun updateContents(contents: String): MemoBottomSheetState

    data class Update(override val uiMemo: UiMemo) : MemoBottomSheetState {
        override fun updateContents(contents: String): MemoBottomSheetState {
            return copy(uiMemo = uiMemo.copy(contents = contents))
        }
    }

    data class Add(override val uiMemo: UiMemo = UiMemo.getEmptyMemo()) : MemoBottomSheetState {
        override fun updateContents(contents: String): MemoBottomSheetState {
            return copy(uiMemo = uiMemo.copy(contents = contents))
        }
    }
}