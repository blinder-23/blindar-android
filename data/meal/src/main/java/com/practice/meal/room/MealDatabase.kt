package com.practice.meal.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MealEntityRoom::class],
    version = 3,
)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
}