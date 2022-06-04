package com.practice.neis.meal.pojo

data class MealResponseModel(
    val header: MealHeader,
    val mealData: List<MealModel>
)
