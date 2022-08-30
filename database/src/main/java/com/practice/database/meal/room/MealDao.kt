package com.practice.database.meal.room

import androidx.room.*

@Dao
interface MealDao {

    @Query("SELECT * FROM meal WHERE date LIKE :yearMonthString || '%%'")
    suspend fun getMeals(yearMonthString: String): List<MealEntityRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntityRoom>)

    @Delete
    suspend fun deleteMeals(meals: List<MealEntityRoom>)

    @Query("DELETE FROM meal WHERE date LIKE :yearMonthString || '%%'")
    suspend fun deleteMeals(yearMonthString: String)

    @Query("DELETE FROM meal")
    suspend fun clear()

}