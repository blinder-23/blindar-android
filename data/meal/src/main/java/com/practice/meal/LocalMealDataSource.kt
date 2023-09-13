package com.practice.meal

import com.hsk.ktx.getDateString
import com.practice.domain.meal.Meal
import com.practice.meal.room.MealDao
import com.practice.meal.room.toMealEntities
import com.practice.meal.room.toRoomEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMealDataSource(private val mealDao: MealDao) : MealDataSource {

    override suspend fun getMeals(schoolCode: Int, year: Int, month: Int): Flow<List<Meal>> {
        return mealDao.getMeals(schoolCode, getDateString(year, month)).map {
//            Log.d("LocalMealDataSource", "meal $year $month: ${it.size}")
            it.toMealEntities()
        }
    }

    override suspend fun insertMeals(meals: List<Meal>) {
//        Log.d("LocalMealDataSource", "Inserted: ${meals[0].year} ${meals[0].month} ${meals.size}")
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