package com.example.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.domain.combine.toMealEntity
import com.practice.database.meal.MealRepository
import com.practice.database.meal.entity.MealEntity
import com.practice.neis.meal.RemoteMealRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@HiltWorker
class FetchRemoteMealWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val localRepository: MealRepository,
    private val remoteRepository: RemoteMealRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            fetchRemoteMeals()
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private suspend fun fetchRemoteMeals(): Result {
        val nowYear = LocalDate.now().year
        (nowYear - 2..nowYear).forEach { year ->
            (1..12).forEach { month ->
                fetchAndStoreMeals(year, month)
            }
        }
        return Result.success()
    }

    private suspend fun fetchAndStoreMeals(year: Int, month: Int) {
        val meals = fetchMeals(year, month)
        storeMeals(meals)
    }

    private suspend fun fetchMeals(year: Int, month: Int) =
        remoteRepository.getMealData(year, month)
            .map { it.toMealEntity() }

    private suspend fun storeMeals(meals: List<MealEntity>) {
        localRepository.insertMeals(meals)
    }

    private fun handleException(e: Exception): Result {
        Log.e("FetchRemoteMealWork", e.message ?: "")
        return Result.failure()
    }
}

private const val fetchRemoteMealWorkTag = "fetch_remote_meal_work"

fun setPeriodicFetchMealWork(workManager: WorkManager) {
    val periodicWork = PeriodicWorkRequestBuilder<FetchRemoteMealWorker>(1, TimeUnit.DAYS)
        .addTag(fetchRemoteMealWorkTag)
        .build()
    workManager.enqueueUniquePeriodicWork(
        fetchRemoteMealWorkTag,
        ExistingPeriodicWorkPolicy.REPLACE,
        periodicWork
    )
}