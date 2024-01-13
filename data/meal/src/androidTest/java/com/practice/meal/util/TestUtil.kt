package com.practice.meal.util

import com.practice.domain.meal.Meal
import com.practice.domain.meal.Menu
import com.practice.domain.meal.Nutrient
import com.practice.domain.meal.Origin
import com.practice.meal.room.MealEntityRoom

object TestUtil {
    val schoolCode = 10
    fun createMealEntityRoom(count: Int): List<MealEntityRoom> {
        return (0 until count).map {
            MealEntityRoom(
                date = (20220601 + it).toString(),
                menu = "menu $it",
                origin = "origin $it",
                calorie = 0.0,
                nutrient = "nutrient $it",
                schoolCode = schoolCode,
                mealName = "meal $it"
            )
        }
    }

    fun createMealEntity(count: Int): List<Meal> =
        (0 until count).map {
            Meal(
                schoolCode = schoolCode,
                year = 2022,
                month = 5,
                day = 1 + it,
                menus = listOf(
                    Menu(
                        name = "menu",
                        allergies = emptyList()
                    )
                ),
                origins = listOf(
                    Origin(
                        ingredient = "rice",
                        origin = "korea"
                    )
                ),
                calorie = 10.0,
                nutrients = listOf(Nutrient("protein", "g", 10.0)),
                mealName = "test meal",
            )
        }
}