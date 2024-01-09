package com.practice.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.hsk.ktx.date.Date
import com.practice.api.meal.RemoteMealRepository
import com.practice.domain.meal.Meal
import com.practice.meal.MealRepository
import com.practice.preferences.PreferencesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class FetchRemoteMealWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val localRepository: MealRepository,
    private val remoteRepository: RemoteMealRepository,
    private val preferencesRepository: PreferencesRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        clearDatabaseIfValueIsSet()
        preferencesRepository.increaseRunningWorkCount()
        val result = fetchRemoteMeals()
        preferencesRepository.decreaseRunningWorkCount()
        Log.d(TAG, "finished!")
        return result
    }

    private suspend fun clearDatabaseIfValueIsSet() {
        val clearDatabase = inputData.getBoolean(clearDatabaseKey, false)
        if (clearDatabase) {
            Log.d(TAG, "clear database")
            localRepository.clear()
        } else {
            Log.d(TAG, "doesn't clear database")
        }
    }

    private suspend fun fetchRemoteMeals(): Result {
        val now = Date.now()
        val currentYear = now.year
        val currentMonth = now.month
        val schoolCode = preferencesRepository.fetchInitialPreferences().schoolCode
        (currentYear downTo currentYear - 2).forEach { year ->
            (0 until 12).forEach { months ->
                tryFetchAndStoreMeals(schoolCode, year, (currentMonth + months - 1) % 12 + 1)
            }
        }
        return Result.success()
    }

    private suspend fun tryFetchAndStoreMeals(schoolCode: Int, year: Int, month: Int) {
        try {
            fetchAndStoreMeals(schoolCode, year, month)
        } catch (e: Exception) {
            handleException(e, year, month)
        }
    }

    private suspend fun fetchAndStoreMeals(schoolCode: Int, year: Int, month: Int) {
        val meals = fetchMeals(schoolCode, year, month)
        storeMeals(meals)
    }

    private suspend fun fetchMeals(schoolCode: Int, year: Int, month: Int): List<Meal> =
        remoteRepository.getMeals(schoolCode, year, month).apply {
            Log.d(TAG, "$schoolCode meal $year $month: ${meals.size}")
        }.meals

    private suspend fun storeMeals(meals: List<Meal>) {
        localRepository.insertMeals(meals)
    }

    private fun handleException(e: Exception, year: Int, month: Int): Result {
        Log.e(TAG, "$year, $month has an exception: ${e.message}")
        return Result.failure()
    }

    companion object {
        fun createData(clearDatabase: Boolean): Data {
            return Data.Builder()
                .putBoolean(clearDatabaseKey, clearDatabase)
                .build()
        }

        private const val clearDatabaseKey = "clear-meal-database"

        private const val TAG = "FetchRemoteMealWorker"
    }
}

const val periodicMealWorkTag = "periodic_meal_work"
const val oneTimeMealWorkTag = "onetime_meal_work"

fun setPeriodicFetchMealWork(workManager: WorkManager) {
    val periodicWork = PeriodicWorkRequestBuilder<FetchRemoteMealWorker>(1, TimeUnit.DAYS)
        .addTag(periodicMealWorkTag)
        .build()
    workManager.enqueueUniquePeriodicWork(
        periodicMealWorkTag,
        ExistingPeriodicWorkPolicy.UPDATE,
        periodicWork
    )
}

fun setOneTimeFetchMealWork(workManager: WorkManager, clearDatabase: Boolean = false) {
    val oneTimeWork = OneTimeWorkRequestBuilder<FetchRemoteMealWorker>()
        .addTag(oneTimeMealWorkTag)
        .setInputData(FetchRemoteMealWorker.createData(clearDatabase))
        .build()
    workManager.enqueueUniqueWork(
        oneTimeMealWorkTag,
        ExistingWorkPolicy.REPLACE,
        oneTimeWork
    )
}