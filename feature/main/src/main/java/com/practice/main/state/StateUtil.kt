package com.practice.main.state

fun mergeSchedulesAndMemos(
    scheduleUiState: ScheduleUiState,
    memoUiState: MemoUiState,
): List<MemoPopupElement> {
    return mutableListOf<MemoPopupElement>().apply {
        addAll(scheduleUiState.uiSchedules)
        addAll(memoUiState.memos)
        sortBy { it.sortOrder }
    }
}