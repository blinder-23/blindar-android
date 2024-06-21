package com.practice.main.nutrient

import com.hsk.ktx.date.Date
import kotlinx.serialization.Serializable

@Serializable
data class NutrientRoute(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val schoolCode: Int,
    val mealTime: String,
)

val NutrientRoute.date: Date
    get() = Date(year, month, dayOfMonth)