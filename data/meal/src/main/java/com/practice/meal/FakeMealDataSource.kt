package com.practice.meal

import com.practice.meal.entity.MealEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMealDataSource : MealDataSource {

    private val mealEntities = mutableSetOf<MealEntity>()

    override suspend fun getMeals(year: Int, month: Int): Flow<List<MealEntity>> {
        return flow {
            emit(mealEntities.filter { it.year == year && it.month == month })
        }
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