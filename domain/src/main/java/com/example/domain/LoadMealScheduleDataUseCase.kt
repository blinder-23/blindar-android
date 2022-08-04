package com.example.domain

import com.practice.database.meal.MealRepository
import com.practice.database.meal.entity.MealEntity
import com.practice.database.schedule.ScheduleRepository
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.neis.meal.MealRemoteRepository
import com.practice.neis.meal.pojo.MealModel
import com.practice.neis.schedule.ScheduleRemoteRepository
import com.practice.neis.schedule.pojo.ScheduleModel

class LoadMealScheduleDataUseCase(
    private val mealLocalRepository: MealRepository,
    private val scheduleLocalRepository: ScheduleRepository,
    private val mealRemoteRepository: MealRemoteRepository,
    private val scheduleRemoteRepository: ScheduleRemoteRepository
) {
    // TODO: 작업을 총괄하는 함수 추가하기

    suspend fun checkMealExists(year: Int, month: Int): Boolean {
        return mealLocalRepository.getMeals(year, month).isNotEmpty()
    }

    suspend fun checkScheduleExists(year: Int, month: Int): Boolean {
        return scheduleLocalRepository.getSchedules(year, month).isNotEmpty()
    }

    suspend fun loadMealFromRemote(year: Int, month: Int): List<MealModel> {
        return mealRemoteRepository.getMealData(year, month)
    }

    suspend fun storeMealToLocal(meals: List<MealModel>) {
        val mealEntities = meals.map { it.toMealEntity() }
        mealLocalRepository.insertMeals(mealEntities)
    }

    suspend fun loadMealFromLocal(year: Int, month: Int): List<MealEntity> {
        return mealLocalRepository.getMeals(year, month)
    }

    suspend fun loadScheduleFromRemote(year: Int, month: Int): List<ScheduleModel> {
        return scheduleRemoteRepository.getSchedules(year, month)
    }

    suspend fun storeScheduleToLocal(schedules: List<ScheduleModel>) {
        val scheduleEntities = schedules.map { it.toScheduleEntity() }
        scheduleLocalRepository.insertSchedules(scheduleEntities)
    }

    suspend fun loadScheduleFromLocal(year: Int, month: Int): List<ScheduleEntity> {
        return scheduleLocalRepository.getSchedules(year, month)
    }
}