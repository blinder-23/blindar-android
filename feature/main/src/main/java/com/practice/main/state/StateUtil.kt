package com.practice.main.state

import com.practice.main.main.state.MemoDialogElement
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun mergeSchedulesAndMemos(
    uiSchedules: UiSchedules,
    uiMemos: UiMemos,
): ImmutableList<MemoDialogElement> {
    return mutableListOf<MemoDialogElement>().apply {
        addAll(uiSchedules.uiSchedules)
        addAll(uiMemos.memos)
        sortBy { it.sortOrder }
    }.toImmutableList()
}