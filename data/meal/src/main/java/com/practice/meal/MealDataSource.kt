package com.practice.meal

import com.practice.domain.meal.Meal
import kotlinx.coroutines.flow.Flow

sealed interface MealDataSource {
    fun getMeals(schoolCode: Int, year: Int, month: Int): Flow<List<Meal>>
    suspend fun getMealsPlain(schoolCode: Int, year: Int, month: Int): List<Meal>

    suspend fun getMeal(schoolCode: Int, year: Int, month: Int, dayOfMonth: Int): List<Meal>

    suspend fun insertMeals(meals: List<Meal>)

    suspend fun deleteMeals(meals: List<Meal>)

    suspend fun deleteMeals(schoolCode: Int, year: Int, month: Int)

    suspend fun clear()
}