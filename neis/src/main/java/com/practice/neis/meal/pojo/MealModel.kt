package com.practice.neis.meal.pojo

data class MealModel(
    val officeCode: String,
    val officeName: String,
    val schoolCode: Int,
    val schoolName: String,
    val mealCode: Int,
    val mealName: String,
    val date: String,
    val numberStudents: Int,
    val menu: List<MenuModel>,
    val originCountries: List<OriginModel>,
    val calorie: Double,
    val nutrients: List<NutrientModel>,
    val fromDate: String,
    val endDate: String,
)
