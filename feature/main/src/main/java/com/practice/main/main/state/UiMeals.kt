package com.practice.main.main.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.io.Serializable

class UiMeals(meals: List<UiMeal>) : Serializable {

    constructor(vararg meals: UiMeal) : this(meals.toList())

    val meals: ImmutableList<UiMeal> = meals.sortMealsInTimeOrder()

    val description: String
        get() = meals.joinToString(", ") { "${it.mealTime}: ${it.description}" }

    val isEmpty: Boolean
        get() = meals.isEmpty()

    val mealTimes: ImmutableList<String>
        get() = meals.map { it.mealTime }.toImmutableList()

    operator fun get(index: Int) = meals[index]

    private fun List<UiMeal>.sortMealsInTimeOrder(): ImmutableList<UiMeal> {
        return this.sortedBy { it.sortOrder }.toImmutableList()
    }
}