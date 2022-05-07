package com.practice.hanbitlunch.api.meal.pojo

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
    val originCountries: List<String>,
    val calorie: Double,
    val nutrients: List<String>,
    val fromDate: String,
    val endDate: String,
)
