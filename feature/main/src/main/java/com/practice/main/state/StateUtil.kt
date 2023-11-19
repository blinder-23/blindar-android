package com.practice.main.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun mergeSchedulesAndMemos(
    scheduleUiState: ScheduleUiState,
    memoUiState: MemoUiState,
): ImmutableList<MemoPopupElement> {
    return mutableListOf<MemoPopupElement>().apply {
        addAll(scheduleUiState.uiSchedules)
        addAll(memoUiState.memos)
        sortBy { it.sortOrder }
    }.toImmutableList()
}