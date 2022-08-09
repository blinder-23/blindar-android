package com.practice.database.meal

import com.practice.database.meal.entity.MealEntity

sealed interface MealDataSource {
    suspend fun getMeals(year: Int, month: Int): List<MealEntity>

    suspend fun insertMeals(meals: List<MealEntity>)

    suspend fun deleteMeals(meals: List<MealEntity>)

    suspend fun deleteMeals(year: Int, month: Int)

    suspend fun clear()
}