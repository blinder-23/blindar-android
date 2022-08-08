package com.example.domain

import com.practice.database.meal.MealRepository
import com.practice.database.meal.entity.MealEntity
import com.practice.database.schedule.ScheduleRepository
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.neis.meal.MealRemoteRepository
import com.practice.neis.meal.pojo.MealModel
import com.practice.neis.schedule.ScheduleRemoteRepository
import com.practice.neis.schedule.pojo.ScheduleModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class LoadMealScheduleDataUseCase(
    private val mealLocalRepository: MealRepository,
    private val scheduleLocalRepository: ScheduleRepository,
    private val mealRemoteRepository: MealRemoteRepository,
    private val scheduleRemoteRepository: ScheduleRemoteRepository
) {
    suspend fun loadData(year: Int, month: Int): MealScheduleEntity {
        val mealData = loadMealData(year, month)
        val scheduleData = loadScheduleData(year, month)
        return MealScheduleEntity(year, month, mealData, scheduleData)
    }

    internal suspend fun loadMealData(year: Int, month: Int): ImmutableList<MealEntity> {
        val exists = checkMealExists(year, month)
        if (!exists) {
            val mealData = loadMealFromRemote(year, month)
            storeMealToLocal(mealData)
        }
        return loadMealFromLocal(year, month).toImmutableList()
    }

    internal suspend fun loadScheduleData(year: Int, month: Int): ImmutableList<ScheduleEntity> {
        val exists = checkScheduleExists(year, month)
        if (!exists) {
            val scheduleData = loadScheduleFromRemote(year, month)
            storeScheduleToLocal(scheduleData)
        }
        return loadScheduleFromLocal(year, month).toImmutableList()
    }

    internal suspend fun checkMealExists(year: Int, month: Int): Boolean {
        return mealLocalRepository.getMeals(year, month).isNotEmpty()
    }

    internal suspend fun checkScheduleExists(year: Int, month: Int): Boolean {
        return scheduleLocalRepository.getSchedules(year, month).isNotEmpty()
    }

    internal suspend fun loadMealFromRemote(year: Int, month: Int): List<MealModel> {
        return mealRemoteRepository.getMealData(year, month)
    }

    internal suspend fun storeMealToLocal(meals: List<MealModel>) {
        val mealEntities = meals.map { it.toMealEntity() }
        mealLocalRepository.insertMeals(mealEntities)
    }

    internal suspend fun loadMealFromLocal(year: Int, month: Int): List<MealEntity> {
        return mealLocalRepository.getMeals(year, month)
    }

    internal suspend fun loadScheduleFromRemote(year: Int, month: Int): List<ScheduleModel> {
        return scheduleRemoteRepository.getSchedules(year, month)
    }

    internal suspend fun storeScheduleToLocal(schedules: List<ScheduleModel>) {
        val scheduleEntities = schedules.map { it.toScheduleEntity() }
        scheduleLocalRepository.insertSchedules(scheduleEntities)
    }

    internal suspend fun loadScheduleFromLocal(year: Int, month: Int): List<ScheduleEntity> {
        return scheduleLocalRepository.getSchedules(year, month)
    }
}