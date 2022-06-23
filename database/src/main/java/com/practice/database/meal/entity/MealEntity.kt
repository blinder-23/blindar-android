package com.practice.database.meal.entity

import com.hsk.ktx.getDateString
import com.hsk.ktx.jsonToList
import com.hsk.ktx.toJson
import com.practice.database.meal.room.MealEntityRoom

data class MealEntity(
    val schoolCode: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val menus: List<MenuEntity>,
    val origins: List<OriginEntity>,
    val calorie: Double,
    val nutrients: List<NutrientEntity>,
)


fun MealEntity.toRoomEntity() = MealEntityRoom(
    date = getDateString(year, month, day),
    menu = menus.toJson(),
    origin = origins.toJson(),
    calorie = calorie,
    nutrient = nutrients.toJson(),
    schoolCode = schoolCode,
)

fun MealEntityRoom.toMealEntity(): MealEntity {
    return MealEntity(
        schoolCode = schoolCode,
        year = date.slice(0..3).toInt(),
        month = date.slice(4..5).toInt(),
        day = date.slice(6..7).toInt(),
        menus = menu.jsonToList(),
        origins = origin.jsonToList(),
        calorie = calorie,
        nutrients = nutrient.jsonToList(),
    )
}

fun List<MealEntity>.toRoomEntities() = map { it.toRoomEntity() }

fun List<MealEntityRoom>.toMealEntities() = map { it.toMealEntity() }