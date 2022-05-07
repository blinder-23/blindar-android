package com.practice.hanbitlunch.api.meal.pojo

data class MealResponse(
    val header: MealHeader,
    val mealData: List<Meal>
)
