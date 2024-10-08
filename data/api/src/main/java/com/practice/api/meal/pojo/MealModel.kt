package com.practice.api.meal.pojo

data class MealModel(
    val ymd: String,
    val dishes: List<MenuModel>,
    val origins: List<OriginModel>,
    val nutrients: List<NutrientModel>,
    val calorie: Double,
    // TODO: undo this
//    @SerializedName("meal_time")
    val mealTime: String,
)
