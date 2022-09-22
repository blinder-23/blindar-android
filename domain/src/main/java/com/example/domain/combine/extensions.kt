package com.example.domain.combine

import com.example.server.schedule.pojo.ScheduleModel
import com.example.server.toEpochDate
import com.practice.database.meal.entity.MealEntity
import com.practice.database.meal.entity.MenuEntity
import com.practice.database.meal.entity.NutrientEntity
import com.practice.database.meal.entity.OriginEntity
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.neis.meal.pojo.MealModel
import com.practice.neis.meal.pojo.MenuModel
import com.practice.neis.meal.pojo.NutrientModel
import com.practice.neis.meal.pojo.OriginModel

fun MealModel.toMealEntity() = MealEntity(
    schoolCode = schoolCode,
    year = date.substring(0..3).toInt(),
    month = date.substring(4..5).toInt(),
    day = date.substring(6..7).toInt(),
    menus = menu.map { it.toMenuEntity() },
    origins = originCountries.map { it.toOriginEntity() },
    nutrients = nutrients.map { it.toNutrientEntity() },
    calorie = calorie,
)

fun MenuModel.toMenuEntity() = MenuEntity(
    name = name,
    allergies = allergic,
)

fun OriginModel.toOriginEntity() = OriginEntity(
    ingredient = ingredient,
    origin = originCountry,
)

fun NutrientModel.toNutrientEntity() = NutrientEntity(
    name = name,
    unit = unit,
    amount = amount,
)

fun ScheduleModel.toScheduleEntity(): ScheduleEntity {
    val date = date.toEpochDate(timeDiff = 9)
    return ScheduleEntity(
        id = id,
        year = date.year,
        month = date.monthValue,
        day = date.dayOfMonth,
        eventName = title,
        eventContent = ""
    )
}

internal val TAG = "domain"