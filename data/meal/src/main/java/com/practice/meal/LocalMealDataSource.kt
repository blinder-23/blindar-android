package com.practice.meal

import com.hsk.ktx.getDateString
import com.practice.domain.meal.Meal
import com.practice.meal.room.MealDao
import com.practice.meal.room.toMealEntities
import com.practice.meal.room.toMealEntity
import com.practice.meal.room.toRoomEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMealDataSource(private val mealDao: MealDao) : MealDataSource {

    override fun getMeals(schoolCode: Int, year: Int, month: Int): Flow<List<Meal>> {
        return mealDao.getMeals(schoolCode, getDateString(year, month)).map {
            it.toMealEntities()
        }
    }

    override suspend fun getMealsPlain(schoolCode: Int, year: Int, month: Int): List<Meal> {
        return mealDao.getMealsPlain(schoolCode, getDateString(year, month)).map {
            it.toMealEntity()
        }
    }

    override suspend fun getMeal(
        schoolCode: Int,
        year: Int,
        month: Int,
        dayOfMonth: Int,
    ): List<Meal> {
        return mealDao.getMeal(schoolCode, getDateString(year, month, dayOfMonth)).map {
            it.toMealEntity()
        }
    }

    override suspend fun insertMeals(meals: List<Meal>) {
        mealDao.insertMeals(meals.toRoomEntities())
    }

    override suspend fun deleteMeals(meals: List<Meal>) {
        mealDao.deleteMeals(meals.toRoomEntities())
    }

    override suspend fun deleteMeals(schoolCode: Int, year: Int, month: Int) {
        mealDao.deleteMeals(schoolCode, getDateString(year, month))
    }

    override suspend fun clear() {
        mealDao.clear()
    }

}