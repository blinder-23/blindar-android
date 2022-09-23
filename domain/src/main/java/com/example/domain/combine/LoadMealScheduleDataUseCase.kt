package com.example.domain.combine

import com.practice.database.meal.MealRepository
import com.practice.database.meal.entity.MealEntity
import com.practice.database.schedule.ScheduleRepository
import com.practice.database.schedule.entity.ScheduleEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * UI layer should depend on this use case to get meal and schedule data.
 */
class LoadMealScheduleDataUseCase(
    private val localMealRepository: MealRepository,
    private val localScheduleRepository: ScheduleRepository,
) {
    suspend fun loadData(year: Int, month: Int): MealScheduleEntity {
        val mealData = loadMealData(year, month)
        val scheduleData = loadScheduleData(year, month)
        Log.d(TAG, "$year, $month: $mealData")
        return MealScheduleEntity(year, month, mealData, scheduleData)
    }

    internal suspend fun loadMealData(year: Int, month: Int): ImmutableList<MealEntity> {
        return loadMealFromLocal(year, month).toImmutableList()
    }

    internal suspend fun loadScheduleData(year: Int, month: Int): ImmutableList<ScheduleEntity> {
        return loadScheduleFromLocal(year, month).toImmutableList()
    }

    internal suspend fun loadMealFromLocal(year: Int, month: Int): List<MealEntity> {
        return localMealRepository.getMeals(year, month)
    }

    internal suspend fun loadScheduleFromLocal(year: Int, month: Int): List<ScheduleEntity> {
        return localScheduleRepository.getSchedules(year, month)
    }
}