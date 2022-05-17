package com.practice.neisapi.meal.pojo

data class Meal(
    val officeCode: String,
    val officeName: String,
    val schoolCode: Int,
    val schoolName: String,
    val mealCode: Int,
    val mealName: String,
    val date: String,
    val numberStudents: Int,
    val menu: List<Menu>,
    val originCountries: List<Origin>,
    val calorie: Double,
    val nutrients: List<Nutrient>,
    val fromDate: String,
    val endDate: String,
)
