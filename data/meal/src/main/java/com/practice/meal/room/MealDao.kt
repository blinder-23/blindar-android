package com.practice.meal.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Query("SELECT * FROM meal WHERE date LIKE :yearMonthString || '%%'")
    fun getMeals(yearMonthString: String): Flow<List<MealEntityRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntityRoom>)

    @Delete
    suspend fun deleteMeals(meals: List<MealEntityRoom>)

    @Query("DELETE FROM meal WHERE date LIKE :yearMonthString || '%%'")
    suspend fun deleteMeals(yearMonthString: String)

    @Query("DELETE FROM meal")
    suspend fun clear()

}