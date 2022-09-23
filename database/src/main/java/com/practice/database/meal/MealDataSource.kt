package com.practice.database.meal

import com.practice.database.meal.entity.MealEntity
import kotlinx.coroutines.flow.Flow

sealed interface MealDataSource {
    suspend fun getMeals(year: Int, month: Int): Flow<List<MealEntity>>

    suspend fun insertMeals(meals: List<MealEntity>)

    suspend fun deleteMeals(meals: List<MealEntity>)

    suspend fun deleteMeals(year: Int, month: Int)

    suspend fun clear()
}