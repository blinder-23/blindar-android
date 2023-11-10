package com.practice.combine

import com.practice.domain.Memo
import com.practice.domain.meal.Meal
import com.practice.domain.schedule.Schedule
import com.practice.meal.MealRepository
import com.practice.memo.MemoRepository
import com.practice.schedule.ScheduleRepository
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
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * UI layer should depend on this use case to get meal and schedule data.
 */
class LoadMonthlyDataUseCase @Inject constructor(
    private val localMealRepository: MealRepository,
    private val localScheduleRepository: ScheduleRepository,
    private val localMemoRepository: MemoRepository,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    suspend fun loadData(schoolCode: Int, year: Int, month: Int): StateFlow<MealScheduleEntity> =
        combine(
            loadMealData(schoolCode, year, month),
            loadScheduleData(schoolCode, year, month)
        ) { mealData, scheduleData ->
            MealScheduleEntity(schoolCode, year, month, mealData, scheduleData)
        }.stateIn(this)

    internal suspend fun loadMealData(
        schoolCode: Int,
        year: Int,
        month: Int
    ): Flow<ImmutableList<Meal>> {
        return loadMealFromLocal(schoolCode, year, month).map {
//            Log.d("CombineUseCase", "meal $year $month: ${it.size}")
            it.toImmutableList()
        }
    }

    internal suspend fun loadScheduleData(
        schoolCode: Int,
        year: Int,
        month: Int,
    ): Flow<ImmutableList<Schedule>> {
        return loadScheduleFromLocal(schoolCode, year, month).map { it.toImmutableList() }
    }

    internal suspend fun loadMemoData(
        userId: String,
        year: Int,
        month: Int
    ): Flow<ImmutableList<Memo>> {
        return loadMemoFromLocal(userId, year, month).map { it.toImmutableList() }
    }

    private suspend fun loadMealFromLocal(
        schoolCode: Int,
        year: Int,
        month: Int
    ): Flow<List<Meal>> {
        return localMealRepository.getMeals(schoolCode, year, month)
    }

    private suspend fun loadScheduleFromLocal(
        schoolCode: Int,
        year: Int,
        month: Int
    ): Flow<List<Schedule>> {
        return localScheduleRepository.getSchedules(schoolCode, year, month)
    }
}