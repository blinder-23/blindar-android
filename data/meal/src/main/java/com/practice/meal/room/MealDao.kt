package com.practice.meal.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Query("SELECT * FROM meal WHERE school_code = :schoolCode AND date LIKE :yearMonthString || '%%'")
    fun getMeals(schoolCode: Int, yearMonthString: String): Flow<List<MealEntityRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntityRoom>)

    @Delete
    suspend fun deleteMeals(meals: List<MealEntityRoom>)

    @Query("DELETE FROM meal WHERE school_code = :schoolCode AND date LIKE :yearMonthString || '%%'")
    suspend fun deleteMeals(schoolCode: Int, yearMonthString: String)

    @Query("DELETE FROM meal")
    suspend fun clear()

}