package com.practice.database.meal

import com.hsk.ktx.getDateString
import com.practice.database.meal.entity.MealEntity
import com.practice.database.meal.room.MealDao
import com.practice.database.meal.room.toMealEntities
import com.practice.database.meal.room.toRoomEntities

class MealLocalDataSource(private val mealDao: MealDao) {

    suspend fun getMeals(year: Int, month: Int): List<MealEntity> {
        return mealDao.getMeals(getDateString(year, month)).toMealEntities()
    }

    suspend fun insertMeals(meals: List<MealEntity>) {
        mealDao.insertMeals(meals.toRoomEntities())
    }

    suspend fun deleteMeals(meals: List<MealEntity>) {
        mealDao.deleteMeals(meals.toRoomEntities())
    }

    suspend fun deleteMeals(year: Int, month: Int) {
        mealDao.deleteMeals(getDateString(year, month))
    }

    suspend fun clear() {
        mealDao.clear()
    }

}