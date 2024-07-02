package com.practice.meal

import com.practice.domain.meal.Meal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMealDataSource : MealDataSource {

    private val meals = mutableSetOf<Meal>()

    override fun getMeals(schoolCode: Int, year: Int, month: Int): Flow<List<Meal>> {
        return flow {
            emit(meals.filter { it.schoolCode == schoolCode && it.year == year && it.month == month })
        }
    }

    override suspend fun getMealsPlain(schoolCode: Int, year: Int, month: Int): List<Meal> {
        return meals.filter {
            it.schoolCode == schoolCode && it.year == year && it.month == month
        }
    }

    override suspend fun getMeal(
        schoolCode: Int,
        year: Int,
        month: Int,
        dayOfMonth: Int,
    ): List<Meal> {
        return meals.filter {
            it.schoolCode == schoolCode && it.year == year && it.month == month && it.day == dayOfMonth
        }
    }

    override suspend fun insertMeals(meals: List<Meal>) {
        print("Add: $meals")
        this.meals.addAll(meals)
    }

    override suspend fun deleteMeals(meals: List<Meal>) {
        this.meals.removeAll(meals.toSet())
    }

    override suspend fun deleteMeals(schoolCode: Int, year: Int, month: Int) {
        meals.removeAll { it.schoolCode == schoolCode && it.year == year && it.month == month }
    }

    override suspend fun clear() {
        meals.clear()
    }
}