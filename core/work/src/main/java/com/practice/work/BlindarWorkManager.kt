package com.practice.work

import android.content.Context
import androidx.work.WorkManager

object BlindarWorkManager {
    fun setPeriodicWork(context: Context) {
        val workManager = WorkManager.getInstance(context)
        setPeriodicFetchScheduleWork(workManager)
        setPeriodicFetchMealWork(workManager)
    }

    fun setOneTimeWork(context: Context) {
        val workManager = WorkManager.getInstance(context)
        setOneTimeFetchScheduleWork(workManager)
        setOneTimeFetchMealWork(workManager)
    }
}