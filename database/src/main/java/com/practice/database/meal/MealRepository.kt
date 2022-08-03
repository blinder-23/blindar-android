package com.practice.database.meal

import com.practice.database.meal.entity.MealEntity

class MealRepository(private val dataSource: MealDataSource) {

    suspend fun getMeals(year: Int, month: Int): List<MealEntity> {
        return dataSource.getMeals(year, month)
    }

    suspend fun insertMeals(meals: List<MealEntity>) {
        dataSource.insertMeals(meals)
    }

    suspend fun insertMeals(vararg meals: MealEntity) {
        insertMeals(meals.asList())
    }

    suspend fun deleteMeals(meals: List<MealEntity>) {
        dataSource.deleteMeals(meals)
    }

    suspend fun deleteMeals(vararg meals: MealEntity) {
        deleteMeals(meals.asList())
    }

    suspend fun deleteMeals(year: Int, month: Int) {
        dataSource.deleteMeals(year, month)
    }

    suspend fun clear() {
        dataSource.clear()
    }

}