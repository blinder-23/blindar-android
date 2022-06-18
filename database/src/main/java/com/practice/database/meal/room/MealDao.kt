package com.practice.database.meal.room

import androidx.room.*

@Dao
interface MealDao {

    @Query("SELECT * FROM meal WHERE date LIKE :yearMonthString || '%%'")
    suspend fun getMeals(yearMonthString: String): List<MealEntityRoom>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMeals(meals: List<MealEntityRoom>)

    @Delete
    fun deleteMeals(meals: List<MealEntityRoom>)

    @Query("DELETE FROM meal WHERE date LIKE :yearMonthString || '%%'")
    fun deleteMeals(yearMonthString: String)

}