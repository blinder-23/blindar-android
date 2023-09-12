package com.practice.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.hsk.ktx.date.Date
import com.practice.api.schedule.RemoteScheduleRepository
import com.practice.domain.schedule.Schedule
import com.practice.preferences.PreferencesRepository
import com.practice.schedule.ScheduleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class FetchRemoteScheduleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val localRepository: ScheduleRepository,
    private val remoteRepository: RemoteScheduleRepository,
    private val preferencesRepository: PreferencesRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        preferencesRepository.increaseRunningWorkCount()
        val result = fetchRemoteSchedules()
        preferencesRepository.decreaseRunningWorkCount()
        Log.d("FetchRemoteScheduleWorker", "finished!")
        return result
    }

    private suspend fun fetchRemoteSchedules(): Result {
        val now = Date.now()
        val currentYear = now.year
        val currentMonth = now.month
        val schoolCode = preferencesRepository.fetchInitialPreferences().schoolCode
        (currentYear downTo currentYear - 2).forEach { year ->
            (0 until 12).forEach { months ->
                tryFetchAndStoreSchedules(schoolCode, year, (currentMonth + months - 1) % 12 + 1)
            }
        }
        return Result.success()
    }

    private suspend fun tryFetchAndStoreSchedules(schoolCode: Int, year: Int, month: Int) {
        try {
            fetchAndStoreSchedules(schoolCode, year, month)
        } catch (e: Exception) {
            handleException(e, year, month)
        }
    }

    private suspend fun fetchAndStoreSchedules(schoolCode: Int, year: Int, month: Int) {
        val schedules = fetchSchedules(schoolCode, year, month)
        storeSchedules(schedules)
    }

    private suspend fun fetchSchedules(schoolCode: Int, year: Int, month: Int): List<Schedule> =
        remoteRepository.getSchedules(schoolCode, year, month).schedules

    private suspend fun storeSchedules(schedules: List<Schedule>) {
        localRepository.insertSchedules(schedules)
    }

    private fun handleException(e: Exception, year: Int, month: Int): Result {
        Log.e("FetchRemoteScheduleWork", "$year, $month has an exception: ${e.message}")
        e.printStackTrace()
        return Result.failure()
    }
}

const val periodicScheduleWorkTag = "periodic_schedule_work"
const val oneTimeScheduleWorkTag = "onetime_schedule_work"

fun setPeriodicFetchScheduleWork(workManager: WorkManager) {
    val periodicWork = PeriodicWorkRequestBuilder<FetchRemoteScheduleWorker>(1, TimeUnit.DAYS)
        .addTag(periodicScheduleWorkTag)
        .build()
    workManager.enqueueUniquePeriodicWork(
        periodicScheduleWorkTag,
        ExistingPeriodicWorkPolicy.KEEP,
        periodicWork
    )
}

fun setOneTimeFetchScheduleWork(workManager: WorkManager) {
    val oneTimeWork = OneTimeWorkRequestBuilder<FetchRemoteScheduleWorker>()
        .addTag(oneTimeScheduleWorkTag)
        .build()
    workManager.enqueueUniqueWork(
        oneTimeScheduleWorkTag,
        ExistingWorkPolicy.KEEP,
        oneTimeWork
    )
}