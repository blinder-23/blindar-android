package com.practice.meal.util

import com.practice.meal.entity.MealEntity
import com.practice.meal.room.MealEntityRoom

object TestUtil {
    fun createMealEntityRoom(count: Int): List<MealEntityRoom> {
        return (0 until count).map {
            MealEntityRoom(
                date = (20220601 + it).toString(),
                menu = "menu $it",
                origin = "origin $it",
                calorie = 0.0,
                nutrient = "nutrient $it",
                schoolCode = 0
            )
        }
    }

    fun createMealEntity(count: Int): List<MealEntity> =
        (0 until count).map {
            com.practice.meal.entity.MealEntity(
                schoolCode = 10,
                year = 2022,
                month = 5,
                day = 1 + it,
                menus = listOf(
                    com.practice.meal.entity.MenuEntity(
                        name = "menu",
                        allergies = emptyList()
                    )
                ),
                origins = listOf(
                    com.practice.meal.entity.OriginEntity(
                        ingredient = "rice",
                        origin = "korea"
                    )
                ),
                calorie = 10.0,
                nutrients = listOf(com.practice.meal.entity.NutrientEntity("protein", "g", 10.0))
            )
        }
}