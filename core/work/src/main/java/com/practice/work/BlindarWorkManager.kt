package com.practice.work

import android.content.Context
import androidx.work.WorkManager

object BlindarWorkManager {
    // TODO: 그냥 work가 아닌 data work로 바꾸기
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

    fun setUserInfoToFirebaseWork(context: Context) {
        UploadUserInfoToFirebaseWork.setOneTimeWork(context)
    }

    fun setFetchMemoFromServerWork(context: Context, userId: String) {
        LoadMemoFromServerWork.setOneTimeWork(context, userId)
    }
}