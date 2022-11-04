package com.example.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.domain.combine.toScheduleEntity
import com.example.server.schedule.RemoteScheduleRepository
import com.practice.database.schedule.ScheduleRepository
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.preferences.PreferencesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
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
        preferencesRepository.updateIsFetching(true)
        val result = fetchRemoteSchedules()
        preferencesRepository.updateIsFetching(false)
        Log.d("FetchRemoteScheduleWorker", "finished!")
        return result
    }

    private suspend fun fetchRemoteSchedules(): Result {
        val now = LocalDate.now()
        val currentYear = now.year
        val currentMonth = now.monthValue
        (currentYear downTo currentYear - 2).forEach { year ->
            (0 until 12).forEach { months ->
                tryFetchAndStoreSchedules(year, (currentMonth + months - 1) % 12 + 1)
            }
        }
        return Result.success()
    }

    private suspend fun tryFetchAndStoreSchedules(year: Int, month: Int) {
        try {
            fetchAndStoreSchedules(year, month)
        } catch (e: Exception) {
            handleException(e, year, month)
        }
    }

    private suspend fun fetchAndStoreSchedules(year: Int, month: Int) {
        val schedules = fetchSchedules(year, month)
        storeSchedules(schedules)
    }

    private suspend fun fetchSchedules(year: Int, month: Int) =
        remoteRepository.getSchedules(year, month).schedules
            .map { it.toScheduleEntity() }

    private suspend fun storeSchedules(schedules: List<ScheduleEntity>) {
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