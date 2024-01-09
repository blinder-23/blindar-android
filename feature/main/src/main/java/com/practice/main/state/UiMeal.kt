package com.practice.main.state

import com.hsk.ktx.date.Date
import com.practice.domain.meal.Meal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

/**
 * Ui state of meal
 */
// TODO: 향후 하루에 여러 개의 식단을 지우너하게 되면 UiMeals 추가하기
data class UiMeal(
    val year: Int,
    val month: Int,
    val day: Int,
    val menus: ImmutableList<Menu>,
    val nutrients: ImmutableList<Nutrient>,
) {
    val isEmpty: Boolean
        get() = menus.isEmpty()

    val description: String
        get() = if (menus.isEmpty()) "식단이 없습니다." else menus.joinToString(", ") { it.name }

    companion object {
        val EmptyUiMeal = UiMeal(
            year = Date.now().year,
            month = Date.now().month,
            day = Date.now().dayOfMonth,
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
        menus = menus.filter { it.name != "급식우유" }.map { Menu(it.name) }.toPersistentList(),
        nutrients = nutrients.map { Nutrient(it.name, it.amount, it.unit) }.toPersistentList()
            .add(0, Nutrient("열량", calorie, "kcal")),
    )
}

data class Menu(
    val name: String,
)

data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String,
) {
    val description: String
        get() = "$name $amount $unit"
}