package com.example.domain.combine

import com.example.server.meal.pojo.MealModel
import com.example.server.meal.pojo.MenuModel
import com.example.server.meal.pojo.NutrientModel
import com.example.server.meal.pojo.OriginModel
import com.example.server.schedule.pojo.ScheduleModel
import com.example.server.toEpochDate
import com.practice.database.meal.entity.MealEntity
import com.practice.database.meal.entity.MenuEntity
import com.practice.database.meal.entity.NutrientEntity
import com.practice.database.meal.entity.OriginEntity
import com.practice.database.schedule.entity.ScheduleEntity

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
    val date = date.toEpochDate(timeDiff = 9)
    return ScheduleEntity(
        id = id,
        year = date.year,
        month = date.monthValue,
        day = date.dayOfMonth,
        eventName = title,
        eventContent = contents
    )
}

internal val TAG = "domain"