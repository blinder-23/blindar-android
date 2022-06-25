package com.practice.database

import com.practice.database.meal.room.MealEntityRoom

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
}