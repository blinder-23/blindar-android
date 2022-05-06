package com.practice.hanbitlunch.api.meal

data class MealResponse(
    val header: MealHeader,
    val mealData: List<Meal>
)
