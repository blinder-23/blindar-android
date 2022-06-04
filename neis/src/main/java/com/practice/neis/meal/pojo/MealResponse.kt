package com.practice.neis.meal.pojo

data class MealResponse(
    val header: MealHeader,
    val mealData: List<Meal>
)
