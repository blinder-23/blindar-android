package com.practice.api.meal.pojo

import com.practice.domain.meal.Meal
import com.practice.domain.meal.Menu
import com.practice.domain.meal.MonthlyMeal
import com.practice.domain.meal.Nutrient
import com.practice.domain.meal.Origin

fun MealResponse.toMonthlyMeal(
    schoolCode: Int,
    year: Int,
    month: Int
) = MonthlyMeal(
    schoolCode = schoolCode,
    year = year,
    month = month,
    meals = response.toMealList(schoolCode),
)

fun List<MealModel>.toMealList(schoolCode: Int) = map { it.toMeal(schoolCode) }

fun MealModel.toMeal(schoolCode: Int) = Meal(
    schoolCode = schoolCode,
    year = ymd.substring(0..3).toInt(),
    month = ymd.substring(4..5).toInt(),
    day = ymd.substring(6..7).toInt(),
    menus = dishes.toMenuList(),
    origins = origins.toOriginList(),
    nutrients = nutrients.toNutrientList(),
    calorie = calorie,
)

fun List<MenuModel>.toMenuList() = map { it.toMenu() }

fun MenuModel.toMenu() = Menu(
    name = menu,
    allergies = allergies,
)

fun List<NutrientModel>.toNutrientList() = map { it.toNutrient() }

fun NutrientModel.toNutrient() = Nutrient(
    name = nutrient,
    unit = unit,
    amount = amount.toDouble(),
)

fun List<OriginModel>.toOriginList() = map { it.toOrigin() }

fun OriginModel.toOrigin() = Origin(
    ingredient = ingredient,
    origin = origin,
)