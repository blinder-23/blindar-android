package com.practice.work

import android.content.Context
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
        clearDatabaseIfValueIsSet()
        preferencesRepository.increaseRunningWorkCount()
        val result = fetchRemoteSchedules()
        preferencesRepository.decreaseRunningWorkCount()
        return result
    }

    private suspend fun clearDatabaseIfValueIsSet() {
        val clearDatabase = inputData.getBoolean(clearDatabaseKey, false)
        if (clearDatabase) {
            localRepository.clear()
        }
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
            handleException(e)
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

    private fun handleException(e: Exception): Result {
        e.printStackTrace()
        return Result.failure()
    }

    companion object {
        fun createData(clearDatabase: Boolean): Data {
            return Data.Builder()
                .putBoolean(clearDatabaseKey, clearDatabase)
                .build()
        }

        private const val clearDatabaseKey = "clear-schedule-database"

        private const val TAG = "FetchRemoteScheduleWorker"
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

fun setOneTimeFetchScheduleWork(workManager: WorkManager, clearDatabase: Boolean = false) {
    val oneTimeWork = OneTimeWorkRequestBuilder<FetchRemoteScheduleWorker>()
        .addTag(oneTimeScheduleWorkTag)
        .setInputData(FetchRemoteScheduleWorker.createData(clearDatabase))
        .build()
    workManager.enqueueUniqueWork(
        oneTimeScheduleWorkTag,
        ExistingWorkPolicy.KEEP,
        oneTimeWork
    )
}