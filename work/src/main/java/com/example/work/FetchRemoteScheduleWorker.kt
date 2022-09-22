package com.example.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.domain.combine.toScheduleEntity
import com.example.server.schedule.RemoteScheduleRepository
import com.practice.database.schedule.ScheduleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@HiltWorker
class FetchRemoteScheduleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val localScheduleRepository: ScheduleRepository,
    private val remoteScheduleRepository: RemoteScheduleRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            tryFetchRemoteSchedules()
        } catch (e: Exception) {
            exceptionWhenFetching(e)
        }
    }

    private suspend fun tryFetchRemoteSchedules(): Result {
        val nowYear = LocalDate.now().year
        (nowYear - 2..nowYear).forEach { year ->
            (1..12).forEach { month ->
                fetchAndStoreSchedules(year, month)
            }
        }
        return Result.success()
    }

    private suspend fun fetchAndStoreSchedules(year: Int, month: Int) {
        val schedules = remoteScheduleRepository.getSchedules(year, month).schedules
            .map { it.toScheduleEntity() }
        localScheduleRepository.insertSchedules(schedules)
    }

    private fun exceptionWhenFetching(e: Exception): Result {
        Log.e("FetchRemoteScheduleWork", e.message ?: "")
        return Result.failure()
    }
}

const val fetchRemoteScheduleWorkTag = "fetch_remote_schedule_work"

fun setPeriodicFetchScheduleWork(workManager: WorkManager) {
    val periodicWork = PeriodicWorkRequestBuilder<FetchRemoteScheduleWorker>(1, TimeUnit.DAYS)
        .addTag(fetchRemoteScheduleWorkTag)
        .build()
    workManager.enqueueUniquePeriodicWork(
        fetchRemoteScheduleWorkTag,
        ExistingPeriodicWorkPolicy.REPLACE,
        periodicWork
    )
}