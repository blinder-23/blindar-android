package com.practice.database.meal.entity

data class MealEntity(
    val schoolCode: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val menus: List<MenuEntity>,
    val origins: List<OriginEntity>,
    val calorie: Double,
    val nutrients: List<NutrientEntity>,
)