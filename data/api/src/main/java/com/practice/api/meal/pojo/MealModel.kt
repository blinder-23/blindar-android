package com.practice.api.meal.pojo

import com.google.gson.annotations.SerializedName

data class MealModel(
    val ymd: String,
    val dishes: List<MenuModel>,
    val origins: List<OriginModel>,
    val nutrients: List<NutrientModel>,
    val calorie: Double,
    @SerializedName("meal_time") val mealTime: String,
)
