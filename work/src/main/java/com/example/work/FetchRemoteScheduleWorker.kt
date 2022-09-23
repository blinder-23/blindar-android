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
import com.practice.database.schedule.entity.ScheduleEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@HiltWorker
class FetchRemoteScheduleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val localRepository: ScheduleRepository,
    private val remoteRepository: RemoteScheduleRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            fetchRemoteSchedules()
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private suspend fun fetchRemoteSchedules(): Result {
        val nowYear = LocalDate.now().year
        (nowYear - 2..nowYear).forEach { year ->
            (1..12).forEach { month ->
                fetchAndStoreSchedules(year, month)
            }
        }
        return Result.success()
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

    private fun handleException(e: Exception): Result {
        Log.e("FetchRemoteScheduleWork", e.message ?: "")
        return Result.failure()
    }
}

private const val fetchRemoteScheduleWorkTag = "fetch_remote_schedule_work"

fun setPeriodicFetchScheduleWork(workManager: WorkManager) {
    val periodicWork = PeriodicWorkRequestBuilder<FetchRemoteScheduleWorker>(1, TimeUnit.DAYS)
        .addTag(fetchRemoteScheduleWorkTag)
        .build()
    workManager.enqueueUniquePeriodicWork(
        fetchRemoteScheduleWorkTag,
        ExistingPeriodicWorkPolicy.KEEP,
        periodicWork
    )
}