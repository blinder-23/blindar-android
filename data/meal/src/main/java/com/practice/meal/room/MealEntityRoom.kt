package com.practice.meal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.hsk.ktx.getDateString
import com.hsk.ktx.jsonToList
import com.hsk.ktx.toJson
import com.practice.domain.meal.Meal

@Entity(tableName = "meal", primaryKeys = ["school_code", "date", "meal_time"])
data class MealEntityRoom(
    val date: String,
    val menu: String,
    val origin: String,
    val calorie: Double,
    val nutrient: String,
    @ColumnInfo(name = "meal_time") val mealTime: String,
    @ColumnInfo(name = "school_code") val schoolCode: Int,
)

val MealEntityRoom.yearMonth: String
    get() = date.substring(0..5)

fun Meal.toRoomEntity() = MealEntityRoom(
    date = getDateString(year, month, day),
    menu = menus.toJson(),
    origin = origins.toJson(),
    calorie = calorie,
    nutrient = nutrients.toJson(),
    schoolCode = schoolCode,
    mealTime = mealTime,
)

fun MealEntityRoom.toMealEntity(): Meal {
    return Meal(
        schoolCode = schoolCode,
        year = date.slice(0..3).toInt(),
        month = date.slice(4..5).toInt(),
        day = date.slice(6..7).toInt(),
        menus = menu.jsonToList(),
        origins = origin.jsonToList(),
        calorie = calorie,
        nutrients = nutrient.jsonToList(),
        mealTime = mealTime,
    )
}

fun List<Meal>.toRoomEntities() = map { it.toRoomEntity() }

fun List<MealEntityRoom>.toMealEntities() = map { it.toMealEntity() }