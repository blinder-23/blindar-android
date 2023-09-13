package com.practice.domain.meal

data class MonthlyMeal(
    val schoolCode: Int,
    val year: Int,
    val month: Int,
    val meals: List<Meal>,
)
