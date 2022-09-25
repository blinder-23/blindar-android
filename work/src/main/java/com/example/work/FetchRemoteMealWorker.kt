package com.example.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.domain.combine.toMealEntity
import com.practice.database.meal.MealRepository
import com.practice.database.meal.entity.MealEntity
import com.practice.neis.meal.RemoteMealRepository
import com.practice.neis.meal.util.MealDeserializerException
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
        return fetchRemoteMeals()
    }

    private suspend fun fetchRemoteMeals(): Result {
        val now = LocalDate.now()
        val currentYear = now.year
        val currentMonth = now.monthValue
        (currentYear downTo currentYear - 2).forEach { year ->
            (0 until 12).forEach { months ->
                tryFetchAndStoreMeals(year, (currentMonth + months - 1) % 12 + 1)
            }
        }
        return Result.success()
    }

    private suspend fun tryFetchAndStoreMeals(year: Int, month: Int) {
        try {
            fetchAndStoreMeals(year, month)
        } catch (e: MealDeserializerException) {
            handleException(e, year, month)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun handleException(e: Exception, year: Int, month: Int): Result {
        Log.e("FetchRemoteMealWorker", "$year, $month has an exception: ${e.message}")
        return Result.failure()
    }
}

const val fetchRemoteMealWorkTag = "fetch_remote_meal_work"

fun setPeriodicFetchMealWork(workManager: WorkManager) {
    val periodicWork = PeriodicWorkRequestBuilder<FetchRemoteMealWorker>(1, TimeUnit.DAYS)
        .addTag(fetchRemoteMealWorkTag)
        .build()
    workManager.enqueueUniquePeriodicWork(
        fetchRemoteMealWorkTag,
        ExistingPeriodicWorkPolicy.KEEP,
        periodicWork
    )
}