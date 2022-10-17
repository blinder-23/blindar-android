package com.practice.database.meal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hsk.ktx.getDateString
import com.hsk.ktx.jsonToList
import com.hsk.ktx.toJson
import com.practice.database.meal.entity.MealEntity

@Entity(tableName = "meal")
data class MealEntityRoom(
    @PrimaryKey val date: String,
    val menu: String,
    val origin: String,
    val calorie: Double,
    val nutrient: String,
    @ColumnInfo(name = "school_code") val schoolCode: Int,
)

val MealEntityRoom.yearMonth: String
    get() = date.substring(0..5)

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