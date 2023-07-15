package com.practice.combine

import com.practice.meal.MealRepository
import com.practice.meal.entity.MealEntity
import com.practice.schedule.ScheduleRepository
import com.practice.schedule.entity.ScheduleEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext

/**
 * UI layer should depend on this use case to get meal and schedule data.
 */
class LoadMealScheduleDataUseCase(
    private val localMealRepository: MealRepository,
    private val localScheduleRepository: ScheduleRepository,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    suspend fun loadData(year: Int, month: Int): StateFlow<MealScheduleEntity> =
        combine(
            loadMealData(year, month),
            loadScheduleData(year, month)
        ) { mealData, scheduleData ->
            MealScheduleEntity(year, month, mealData, scheduleData)
        }.stateIn(this)

    internal suspend fun loadMealData(year: Int, month: Int): Flow<ImmutableList<MealEntity>> {
        return loadMealFromLocal(year, month).map {
//            Log.d("CombineUseCase", "meal $year $month: ${it.size}")
            it.toImmutableList()
        }
    }

    internal suspend fun loadScheduleData(
        year: Int,
        month: Int
    ): Flow<ImmutableList<ScheduleEntity>> {
        return loadScheduleFromLocal(year, month).map { it.toImmutableList() }
    }

    private suspend fun loadMealFromLocal(year: Int, month: Int): Flow<List<MealEntity>> {
        return localMealRepository.getMeals(year, month)
    }

    private suspend fun loadScheduleFromLocal(year: Int, month: Int): Flow<List<ScheduleEntity>> {
        return localScheduleRepository.getSchedules(year, month)
    }
}