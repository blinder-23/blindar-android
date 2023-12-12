package com.practice.meal

import com.hsk.ktx.date.Date
import com.practice.domain.meal.Meal
import kotlinx.coroutines.flow.Flow

class MealRepository(private val dataSource: MealDataSource) {

    suspend fun getMeals(schoolCode: Int, year: Int, month: Int): Flow<List<Meal>> {
        return dataSource.getMeals(schoolCode, year, month)
    }

    suspend fun getMeal(schoolCode: Int, date: Date): List<Meal> {
        return getMeal(schoolCode, date.year, date.month, date.dayOfMonth)
    }

    suspend fun getMeal(schoolCode: Int, year: Int, month: Int, day: Int): List<Meal> {
        return dataSource.getMeal(schoolCode, year, month, day)
    }

    suspend fun insertMeals(meals: List<Meal>) {
        dataSource.insertMeals(meals)
    }

    suspend fun insertMeals(vararg meals: Meal) {
        insertMeals(meals.asList())
    }

    suspend fun deleteMeals(meals: List<Meal>) {
        dataSource.deleteMeals(meals)
    }

    suspend fun deleteMeals(vararg meals: Meal) {
        deleteMeals(meals.asList())
    }

    suspend fun deleteMeals(schoolCode: Int, year: Int, month: Int) {
        dataSource.deleteMeals(schoolCode, year, month)
    }

    suspend fun clear() {
        dataSource.clear()
    }

}