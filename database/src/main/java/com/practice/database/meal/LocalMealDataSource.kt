package com.practice.database.meal

import com.hsk.ktx.getDateString
import com.practice.database.meal.entity.MealEntity
import com.practice.database.meal.room.MealDao
import com.practice.database.meal.room.toMealEntities
import com.practice.database.meal.room.toRoomEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMealDataSource(private val mealDao: MealDao) : MealDataSource {

    override suspend fun getMeals(year: Int, month: Int): Flow<List<MealEntity>> {
        return mealDao.getMeals(getDateString(year, month)).map { it.toMealEntities() }
    }

    override suspend fun insertMeals(meals: List<MealEntity>) {
        mealDao.insertMeals(meals.toRoomEntities())
    }

    override suspend fun deleteMeals(meals: List<MealEntity>) {
        mealDao.deleteMeals(meals.toRoomEntities())
    }

    override suspend fun deleteMeals(year: Int, month: Int) {
        mealDao.deleteMeals(getDateString(year, month))
    }

    override suspend fun clear() {
        mealDao.clear()
    }

}