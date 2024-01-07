package com.practice.main.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun mergeSchedulesAndMemos(
    uiSchedules: UiSchedules,
    memoUiState: MemoUiState,
): ImmutableList<MemoPopupElement> {
    return mutableListOf<MemoPopupElement>().apply {
        addAll(uiSchedules.uiSchedules)
        addAll(memoUiState.memos)
        sortBy { it.sortOrder }
    }.toImmutableList()
}