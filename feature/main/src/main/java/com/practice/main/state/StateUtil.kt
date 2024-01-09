package com.practice.main.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun mergeSchedulesAndMemos(
    uiSchedules: UiSchedules,
    uiMemos: UiMemos,
): ImmutableList<MemoPopupElement> {
    return mutableListOf<MemoPopupElement>().apply {
        addAll(uiSchedules.uiSchedules)
        addAll(uiMemos.memos)
        sortBy { it.sortOrder }
    }.toImmutableList()
}