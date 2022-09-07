package com.practice.database.meal

import com.practice.database.meal.entity.MealEntity

class FakeMealDataSource : MealDataSource {

    private val mealEntities = mutableSetOf<MealEntity>()

    override suspend fun getMeals(year: Int, month: Int): List<MealEntity> {
        return mealEntities.filter { it.year == year && it.month == month }
    }

    override suspend fun insertMeals(meals: List<MealEntity>) {
        mealEntities.addAll(meals)
    }

    override suspend fun deleteMeals(meals: List<MealEntity>) {
        mealEntities.removeAll(meals.toSet())
    }

    override suspend fun deleteMeals(year: Int, month: Int) {
        mealEntities.removeAll { it.year == year && it.month == month }
    }

    override suspend fun clear() {
        mealEntities.clear()
    }
}