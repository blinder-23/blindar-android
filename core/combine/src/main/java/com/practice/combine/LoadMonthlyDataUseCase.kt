package com.practice.combine

import com.hsk.ktx.date.Date
import com.practice.api.memo.RemoteMemoRepository
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
import kotlinx.coroutines.flow.SharingStarted
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
    private val remoteMemoRepository: RemoteMemoRepository,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    fun loadData(
        userId: String,
        schoolCode: Int,
        year: Int,
        month: Int,
    ): StateFlow<MonthlyData> = combine(
        loadMealData(schoolCode, year, month),
        loadScheduleData(schoolCode, year, month),
        loadMemoData(userId, year, month),
    ) { mealData, scheduleData, memoData ->
        MonthlyData(schoolCode, year, month, mealData, scheduleData, memoData)
    }.stateIn(this, SharingStarted.Lazily, MonthlyData.Empty)

    internal fun loadMealData(
        schoolCode: Int,
        year: Int,
        month: Int
    ): Flow<ImmutableList<Meal>> {
        return loadMealFromLocal(schoolCode, year, month).map {
            it.toImmutableList()
        }
    }

    internal fun loadScheduleData(
        schoolCode: Int,
        year: Int,
        month: Int,
    ): Flow<ImmutableList<Schedule>> {
        return loadScheduleFromLocal(schoolCode, year, month).map { it.toImmutableList() }
    }

    internal fun loadMemoData(
        userId: String,
        year: Int,
        month: Int
    ): Flow<ImmutableList<Memo>> {
        return loadMemoFromLocal(userId, year, month).map { it.toImmutableList() }
    }

    private fun loadMealFromLocal(
        schoolCode: Int,
        year: Int,
        month: Int
    ): Flow<List<Meal>> {
        return localMealRepository.getMeals(schoolCode, year, month)
    }

    private fun loadScheduleFromLocal(
        schoolCode: Int,
        year: Int,
        month: Int
    ): Flow<List<Schedule>> {
        return localScheduleRepository.getSchedules(schoolCode, year, month)
    }

    private fun loadMemoFromLocal(userId: String, year: Int, month: Int): Flow<List<Memo>> {
        return localMemoRepository.getMemos(userId, year, month)
    }

    suspend fun updateMemoToRemote(userId: String, date: Date, updateItems: List<Memo>) {
        val savedMemos = localMemoRepository.getMemos(userId, date)
        deleteMemosFromRemote(savedMemos)
        localMemoRepository.deleteMemo(userId, date)

        insertMemosOnRemoteAndLocal(updateItems)
    }

    private suspend fun deleteMemosFromRemote(memos: List<Memo>) {
        memos.forEach { memo ->
            remoteMemoRepository.deleteMemo(memo.id)
        }
    }

    private suspend fun insertMemosOnRemoteAndLocal(memos: List<Memo>) {
        memos.filter { it.content.isNotEmpty() }.forEach { memo ->
            val idBackup = memo.id
            val assignedIdFromServer = tryUpdateMemoOnRemote(memo.copy(id = ""))
            localMemoRepository.insertMemo(
                memo.copy(
                    id = assignedIdFromServer ?: idBackup,
                    isSavedOnRemote = assignedIdFromServer != null,
                )
            )
        }
    }

    private suspend fun tryUpdateMemoOnRemote(memo: Memo): String? {
        return try {
            remoteMemoRepository.updateMemo(memo)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteMemo(memo: Memo) {
        deleteMemo(memo.id)
    }

    suspend fun deleteMemo(id: String): String {
        val message = remoteMemoRepository.deleteMemo(id)
        localMemoRepository.deleteMemo(id)
        return message
    }
}