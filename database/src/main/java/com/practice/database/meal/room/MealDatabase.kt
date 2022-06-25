package com.practice.database.meal.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MealEntityRoom::class], version = 1)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
}