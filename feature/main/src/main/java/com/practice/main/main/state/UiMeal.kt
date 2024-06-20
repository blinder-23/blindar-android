package com.practice.main.main.state

import com.hsk.ktx.date.Date
import com.practice.domain.meal.Meal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

/**
 * Ui state of meal
 */
data class UiMeal(
    val year: Int,
    val month: Int,
    val day: Int,
    val mealTime: String,
    val menus: ImmutableList<UiMenu>,
    val nutrients: ImmutableList<UiNutrient>,
) {
    val isEmpty: Boolean
        get() = menus.isEmpty()

    val description: String
        get() = if (menus.isEmpty()) "식단이 없습니다." else menus.joinToString(", ") { it.name }

    val sortOrder: Int
        get() = when (mealTime) {
            "조식" -> 0
            "중식" -> 1
            "석식" -> 2
            else -> 10
        }

    companion object {
        val EmptyUiMeal = UiMeal(
            year = Date.now().year,
            month = Date.now().month,
            day = Date.now().dayOfMonth,
            mealTime = "",
            menus = persistentListOf(),
            nutrients = persistentListOf(),
        )
    }
}

fun Meal.toMealUiState() = if (this.menus.isEmpty()) {
    UiMeal.EmptyUiMeal
} else {
    UiMeal(
        year = year,
        month = month,
        day = day,
        mealTime = mealTime,
        menus = menus.filter { it.name != "급식우유" }.map { UiMenu(it.name) }.toPersistentList(),
        nutrients = nutrients.toUiNutrient(calorie),
    )
}

