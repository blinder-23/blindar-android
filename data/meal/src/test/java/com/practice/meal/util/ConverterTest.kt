package com.practice.meal.util

import com.practice.domain.meal.Meal
import com.practice.domain.meal.Menu
import com.practice.domain.meal.Nutrient
import com.practice.domain.meal.Origin
import com.practice.meal.room.toMealEntity
import com.practice.meal.room.toRoomEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConverterTest {
    @Test
    fun mealEntity_toRoomEntity() {
        val meal = Meal(
            schoolCode = 1,
            year = 2000,
            month = 1,
            day = 3,
            menus = listOf(Menu("menu", listOf(1))),
            origins = listOf(Origin("ingredient", "korea")),
            calorie = 10.0,
            nutrients = listOf(Nutrient("protein", "g", 10.0)),
            mealTime = "중식",
        )
        val expected = com.practice.meal.room.MealEntityRoom(
            date = "20000103",
            menu = """[{"name":"menu","allergies":[1]}]""",
            origin = """[{"ingredient":"ingredient","origin":"korea"}]""",
            calorie = meal.calorie,
            nutrient = """[{"name":"protein","unit":"g","amount":10.0}]""",
            schoolCode = meal.schoolCode,
            mealTime = meal.mealTime,
        )
        assertThat(meal.toRoomEntity()).isEqualTo(expected)
    }

    @Test
    fun roomMealEntity_toMealEntity() {
        val roomMealEntity = com.practice.meal.room.MealEntityRoom(
            date = "20000103",
            menu = """[{"name":"menu","allergies":[1]}]""",
            origin = """[{"ingredient":"ingredient","origin":"korea"}]""",
            calorie = 10.0,
            nutrient = """[{"name":"protein","unit":"g","amount":10.0}]""",
            schoolCode = 1,
            mealTime = "중식",
        )
        val expected = Meal(
            schoolCode = roomMealEntity.schoolCode,
            year = 2000,
            month = 1,
            day = 3,
            menus = listOf(Menu("menu", listOf(1))),
            origins = listOf(Origin("ingredient", "korea")),
            calorie = roomMealEntity.calorie,
            nutrients = listOf(Nutrient("protein", "g", 10.0)),
            mealTime = "중식",
        )
        assertThat(roomMealEntity.toMealEntity()).isEqualTo(expected)
    }
}