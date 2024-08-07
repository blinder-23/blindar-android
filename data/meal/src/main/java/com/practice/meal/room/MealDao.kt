package com.practice.meal.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// TODO: getMeals -> getMealsAsFlow, getMealsPlain -> getMeals로 rename (다른 DAO, repository도 동일)
@Dao
interface MealDao {

    @Query("SELECT * FROM meal WHERE school_code = :schoolCode AND date LIKE :yearMonthString || '%%'")
    fun getMeals(schoolCode: Int, yearMonthString: String): Flow<List<MealEntityRoom>>

    @Query("SELECT * FROM meal WHERE school_code = :schoolCode AND date LIKE :yearMonthString || '%%'")
    suspend fun getMealsPlain(schoolCode: Int, yearMonthString: String): List<MealEntityRoom>

    @Query("SELECT * FROM meal WHERE school_code = :schoolCode AND date = :dateString")
    suspend fun getMeal(schoolCode: Int, dateString: String): List<MealEntityRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntityRoom>)

    @Delete
    suspend fun deleteMeals(meals: List<MealEntityRoom>)

    @Query("DELETE FROM meal WHERE school_code = :schoolCode AND date LIKE :yearMonthString || '%%'")
    suspend fun deleteMeals(schoolCode: Int, yearMonthString: String)

    @Query("DELETE FROM meal")
    suspend fun clear()

}