package com.practice.database.meal.entity

import com.practice.database.meal.room.MealEntityRoom
import com.practice.database.meal.room.toMealEntity
import com.practice.database.meal.room.toRoomEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConverterTest {
    @Test
    fun mealEntity_toRoomEntity() {
        val mealEntity = MealEntity(
            schoolCode = 1,
            year = 2000,
            month = 1,
            day = 3,
            menus = listOf(MenuEntity("menu", listOf(1))),
            origins = listOf(OriginEntity("ingredient", "korea")),
            calorie = 10.0,
            nutrients = listOf(NutrientEntity("protein", "g", 10.0))
        )
        val expected = MealEntityRoom(
            date = "20000103",
            menu = """[{"name":"menu","allergies":[1]}]""",
            origin = """[{"ingredient":"ingredient","origin":"korea"}]""",
            calorie = mealEntity.calorie,
            nutrient = """[{"name":"protein","unit":"g","amount":10.0}]""",
            schoolCode = mealEntity.schoolCode
        )
        assertEquals(expected, mealEntity.toRoomEntity())
    }

    @Test
    fun roomMealEntity_toMealEntity() {
        val roomMealEntity = MealEntityRoom(
            date = "20000103",
            menu = """[{"name":"menu","allergies":[1]}]""",
            origin = """[{"ingredient":"ingredient","origin":"korea"}]""",
            calorie = 10.0,
            nutrient = """[{"name":"protein","unit":"g","amount":10.0}]""",
            schoolCode = 1
        )
        val expected = MealEntity(
            schoolCode = roomMealEntity.schoolCode,
            year = 2000,
            month = 1,
            day = 3,
            menus = listOf(MenuEntity("menu", listOf(1))),
            origins = listOf(OriginEntity("ingredient", "korea")),
            calorie = roomMealEntity.calorie,
            nutrients = listOf(NutrientEntity("protein", "g", 10.0))
        )
        assertEquals(expected, roomMealEntity.toMealEntity())
    }
}