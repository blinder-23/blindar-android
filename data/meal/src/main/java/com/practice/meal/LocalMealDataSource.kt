package com.practice.meal

import com.hsk.ktx.getDateString
import com.practice.meal.entity.MealEntity
import com.practice.meal.room.MealDao
import com.practice.meal.room.toMealEntities
import com.practice.meal.room.toRoomEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMealDataSource(private val mealDao: MealDao) : MealDataSource {

    override suspend fun getMeals(year: Int, month: Int): Flow<List<MealEntity>> {
        return mealDao.getMeals(getDateString(year, month)).map {
//            Log.d("LocalMealDataSource", "meal $year $month: ${it.size}")
            it.toMealEntities()
        }
    }

    override suspend fun insertMeals(meals: List<MealEntity>) {
//        Log.d("LocalMealDataSource", "Inserted: ${meals[0].year} ${meals[0].month} ${meals.size}")
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