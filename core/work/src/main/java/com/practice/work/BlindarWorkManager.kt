package com.practice.work

import android.content.Context
import androidx.work.WorkManager

object BlindarWorkManager {
    fun setPeriodicFetchDataWork(context: Context) {
        val workManager = WorkManager.getInstance(context)
        setPeriodicFetchScheduleWork(workManager)
        setPeriodicFetchMealWork(workManager)
    }

    fun setOneTimeFetchDataWork(context: Context) {
        val workManager = WorkManager.getInstance(context)
        setOneTimeFetchScheduleWork(workManager)
        setOneTimeFetchMealWork(workManager)
    }

    fun setUserInfoToRemoteWork(context: Context) {
        UploadUserInfoWork.setOneTimeWork(context)
    }

    fun setFetchMemoFromServerWork(context: Context, userId: String) {
        LoadMemoFromServerWork.setOneTimeWork(context, userId)
    }
}