package com.practice.database.meal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal")
data class MealEntityRoom(
    @PrimaryKey val date: String,
    val menu: String,
    val origin: String,
    val calorie: Double,
    val nutrient: String,
    @ColumnInfo(name = "school_code") val schoolCode: Int,
)