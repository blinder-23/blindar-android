package com.example.domain.combine

import android.util.Log
import com.practice.database.meal.MealRepository
import com.practice.database.meal.entity.MealEntity
import com.practice.database.schedule.ScheduleRepository
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.neis.meal.RemoteMealRepository
import com.practice.neis.meal.pojo.MealModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * UI layer should depend on this use case to get meal and schedule data.
 */
class LoadMealScheduleDataUseCase(
    private val localMealRepository: MealRepository,
    private val localScheduleRepository: ScheduleRepository,
    private val remoteMealRepository: RemoteMealRepository,
) {
    suspend fun loadData(year: Int, month: Int): MealScheduleEntity {
        val mealData = loadMealData(year, month)
        val scheduleData = loadScheduleData(year, month)
        return MealScheduleEntity(year, month, mealData, scheduleData)
    }

    internal suspend fun loadMealData(year: Int, month: Int): ImmutableList<MealEntity> {
        val mealData = loadMealFromRemote(year, month)
        storeMealToLocal(mealData)
        return loadMealFromLocal(year, month).toImmutableList()
    }

    internal suspend fun loadScheduleData(year: Int, month: Int): ImmutableList<ScheduleEntity> {
        return loadScheduleFromLocal(year, month).toImmutableList()
    }

    internal suspend fun checkMealExists(year: Int, month: Int): Boolean {
        return localMealRepository.getMeals(year, month).isNotEmpty()
    }

    internal suspend fun checkScheduleExists(year: Int, month: Int): Boolean {
        return localScheduleRepository.getSchedules(year, month).isNotEmpty()
    }

    internal suspend fun loadMealFromRemote(year: Int, month: Int): List<MealModel> {
        return try {
            remoteMealRepository.getMealData(year, month)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
            emptyList()
        }
    }

    internal suspend fun storeMealToLocal(meals: List<MealModel>) {
        val mealEntities = meals.map { it.toMealEntity() }
        localMealRepository.insertMeals(mealEntities)
    }

    internal suspend fun loadMealFromLocal(year: Int, month: Int): List<MealEntity> {
        return localMealRepository.getMeals(year, month)
    }

    internal suspend fun loadScheduleFromLocal(year: Int, month: Int): List<ScheduleEntity> {
        return localScheduleRepository.getSchedules(year, month)
    }
}