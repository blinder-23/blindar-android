package com.example.server.meal.pojo

data class MealModel(
    val ymd:String,
    val dishes:List<MenuModel>,
    val origins:List<OriginModel>,
    val nutrients:List<NutrientModel>,
)
