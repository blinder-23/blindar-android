package com.practice.neisapi.meal.pojo

data class MealResponse(
    val header: MealHeader,
    val mealData: List<Meal>
)
