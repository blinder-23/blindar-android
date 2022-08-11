package com.example.domain.combine

import com.practice.database.meal.entity.MealEntity
import com.practice.database.meal.entity.MenuEntity
import com.practice.database.meal.entity.NutrientEntity
import com.practice.database.meal.entity.OriginEntity
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.neis.meal.pojo.MealModel
import com.practice.neis.meal.pojo.MenuModel
import com.practice.neis.meal.pojo.NutrientModel
import com.practice.neis.meal.pojo.OriginModel
import com.practice.neis.schedule.pojo.ScheduleModel

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

fun ScheduleModel.toScheduleEntity() = ScheduleEntity(
    schoolCode = schoolCode,
    year = yearMonthDay.substring(0..3).toInt(),
    month = yearMonthDay.substring(4..5).toInt(),
    day = yearMonthDay.substring(6..7).toInt(),
    eventName = eventName,
    eventContent = eventContent ?: ""
)