package com.practice.domain.meal

data class Meal(
    val schoolCode: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val menus: List<Menu>,
    val origins: List<Origin>,
    val calorie: Double,
    val nutrients: List<Nutrient>,
)