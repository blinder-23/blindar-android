package com.practice.combine

import com.practice.api.meal.pojo.MealModel
import com.practice.api.meal.pojo.MenuModel
import com.practice.api.meal.pojo.NutrientModel
import com.practice.api.meal.pojo.OriginModel
import com.practice.api.schedule.pojo.ScheduleModel
import com.practice.api.toEpochDate
import com.practice.meal.entity.MealEntity
import com.practice.meal.entity.MenuEntity
import com.practice.meal.entity.NutrientEntity
import com.practice.meal.entity.OriginEntity
import com.practice.schedule.entity.ScheduleEntity

// TODO: implement calorie from server
fun MealModel.toMealEntity() = MealEntity(
    schoolCode = 7010578,
    year = ymd.substring(0..3).toInt(),
    month = ymd.substring(4..5).toInt(),
    day = ymd.substring(6..7).toInt(),
    menus = dishes.map { it.toMenuEntity() },
    origins = origins.map { it.toOriginEntity() },
    nutrients = nutrients.map { it.toNutrientEntity() },
    calorie = 0.0,
)

fun MenuModel.toMenuEntity() = MenuEntity(
    name = menu,
    allergies = allergies,
)

fun OriginModel.toOriginEntity() = OriginEntity(
    ingredient = ingredient,
    origin = origin,
)

fun NutrientModel.toNutrientEntity() = NutrientEntity(
    name = nutrient,
    unit = unit,
    amount = amount.toDouble(),
)

fun ScheduleModel.toScheduleEntity(): ScheduleEntity {
    val date = date.toEpochDate(hourDiff = 9)
    return ScheduleEntity(
        id = id,
        year = date.year,
        month = date.month,
        day = date.dayOfMonth,
        eventName = title,
        eventContent = contents
    )
}

internal val TAG = "domain"