package com.practice.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hsk.ktx.date.Date
import com.practice.domain.meal.Meal
import com.practice.meal.MealRepository
import com.practice.memo.MemoRepository
import com.practice.notification.BlindarNotificationManager
import com.practice.preferences.PreferencesRepository
import com.practice.schedule.ScheduleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

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
        if (isNotificationEnabled()) {
            logNotificationIsEnabled()
            sendDailyNotification(context)
        } else {
            logNotificationIsDisabled()
        }
        return Result.success()
    }

    private fun isNotificationEnabled(): Boolean {
        return preferencesRepository.userPreferencesFlow.value.isNotificationEnabled
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
    }
}