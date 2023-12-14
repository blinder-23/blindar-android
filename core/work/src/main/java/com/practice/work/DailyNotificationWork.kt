package com.practice.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.hsk.ktx.date.Date
import com.practice.domain.meal.Meal
import com.practice.meal.MealRepository
import com.practice.memo.MemoRepository
import com.practice.notification.BlindarNotificationManager
import com.practice.preferences.PreferencesRepository
import com.practice.schedule.ScheduleRepository
import com.practice.util.date.DateUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class DailyNotificationWork @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val localMealRepository: MealRepository,
    private val localScheduleRepository: ScheduleRepository,
    private val localMemoRepository: MemoRepository,
    private val preferencesRepository: PreferencesRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return if (isNotificationEnabled()) {
            logNotificationIsEnabled()
            sendDailyNotification(context)
            Result.success()
        } else {
            logNotificationIsDisabled()
            Result.failure()
        }
    }

    private fun isNotificationEnabled(): Boolean {
        return preferencesRepository.userPreferencesFlow.value.isDailyAlarmEnabled
    }

    private suspend fun sendDailyNotification(context: Context) {
        // TODO: 데이터 받아서 알림으로
        sendDailyMealNotification(context)
    }

    private fun logNotificationIsEnabled() {
        log("Notification is enabled at ${Date.now()}")
    }

    private fun logNotificationIsDisabled() {
        log("Notification is disabled at ${Date.now()}")
    }

    private suspend fun sendDailyMealNotification(context: Context) {
        val meals = getDailyMealData()
        if (meals.isNotEmpty()) {
            BlindarNotificationManager.createMealNotification(context, date, meals)
        }
    }

    private suspend fun getDailyMealData(): List<Meal> {
        val schoolId = preferencesRepository.userPreferencesFlow.value.schoolCode
        val now = Date.now()
        return localMealRepository.getMeal(schoolId, now)
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        private const val TAG = "DailyNotificationWork"
        private const val workTag = "daily-notification-work"

        fun setPeriodicDailyNotificationWork(workManager: WorkManager) {
            val periodicWork = PeriodicWorkRequestBuilder<DailyNotificationWork>(1, TimeUnit.DAYS)
                .setInitialDelay(getInitialDelay(), TimeUnit.MILLISECONDS)
                .addTag(workTag)
                .build()
            workManager.enqueueUniquePeriodicWork(
                workTag,
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWork
            )
        }

        fun setOneTimeDailyNotificationWork(workManager: WorkManager) {
            val oneTimeWOrk = OneTimeWorkRequestBuilder<DailyNotificationWork>()
                .addTag(workTag + "dtd")
                .build()
            workManager.enqueueUniqueWork(workTag+"dtd", ExistingWorkPolicy.REPLACE, oneTimeWOrk)
        }
    }
}

private fun getInitialDelay(): Long {
    return DateUtil.getNextTimeOffsetInMillis(8, 0, 0)
}