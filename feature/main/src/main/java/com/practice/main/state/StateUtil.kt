package com.practice.main.state

import com.practice.domain.meal.Nutrient
import com.practice.main.main.state.MemoDialogElement
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

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

fun List<Nutrient>.toUiNutrient(calorie: Double): ImmutableList<UiNutrient> =
    map { UiNutrient(it.name, it.amount, it.unit) }
        .toPersistentList()
        .add(0, UiNutrient("열량", calorie, "kcal"))