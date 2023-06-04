package com.practice.meal.util

import com.practice.meal.room.toMealEntity
import com.practice.meal.room.toRoomEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConverterTest {
    @Test
    fun mealEntity_toRoomEntity() {
        val mealEntity = com.practice.meal.entity.MealEntity(
            schoolCode = 1,
            year = 2000,
            month = 1,
            day = 3,
            menus = listOf(com.practice.meal.entity.MenuEntity("menu", listOf(1))),
            origins = listOf(com.practice.meal.entity.OriginEntity("ingredient", "korea")),
            calorie = 10.0,
            nutrients = listOf(com.practice.meal.entity.NutrientEntity("protein", "g", 10.0))
        )
        val expected = com.practice.meal.room.MealEntityRoom(
            date = "20000103",
            menu = """[{"name":"menu","allergies":[1]}]""",
            origin = """[{"ingredient":"ingredient","origin":"korea"}]""",
            calorie = mealEntity.calorie,
            nutrient = """[{"name":"protein","unit":"g","amount":10.0}]""",
            schoolCode = mealEntity.schoolCode
        )
        assertThat(mealEntity.toRoomEntity()).isEqualTo(expected)
    }

    @Test
    fun roomMealEntity_toMealEntity() {
        val roomMealEntity = com.practice.meal.room.MealEntityRoom(
            date = "20000103",
            menu = """[{"name":"menu","allergies":[1]}]""",
            origin = """[{"ingredient":"ingredient","origin":"korea"}]""",
            calorie = 10.0,
            nutrient = """[{"name":"protein","unit":"g","amount":10.0}]""",
            schoolCode = 1
        )
        val expected = com.practice.meal.entity.MealEntity(
            schoolCode = roomMealEntity.schoolCode,
            year = 2000,
            month = 1,
            day = 3,
            menus = listOf(com.practice.meal.entity.MenuEntity("menu", listOf(1))),
            origins = listOf(com.practice.meal.entity.OriginEntity("ingredient", "korea")),
            calorie = roomMealEntity.calorie,
            nutrients = listOf(com.practice.meal.entity.NutrientEntity("protein", "g", 10.0))
        )
        assertThat(roomMealEntity.toMealEntity()).isEqualTo(expected)
    }
}