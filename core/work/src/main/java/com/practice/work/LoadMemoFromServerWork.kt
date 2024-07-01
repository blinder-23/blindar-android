package com.practice.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.practice.api.memo.RemoteMemoRepository
import com.practice.memo.MemoRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LoadMemoFromServerWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val remoteMemoRepository: RemoteMemoRepository,
    private val localMemoRepository: MemoRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val userId = workerParams.inputData.getString(USER_ID_KEY)
            ?: return Result.failure(workerParams.inputData)

        val memos = remoteMemoRepository.getAllMemos(userId)

        localMemoRepository.insertMemos(memos)
        return Result.success()
    }

    companion object {
        private const val USER_ID_KEY = "user-id-key"
        private const val workerTag = "load-memo-from-server"

        private const val TAG = "LoadMemoFromServerWork"

        fun setOneTimeWork(context: Context, userId: String) {
            val data = Data.Builder()
                .putString(USER_ID_KEY, userId)
            val workManager = WorkManager.getInstance(context)
            val oneTimeWork = OneTimeWorkRequestBuilder<LoadMemoFromServerWork>()
                .addTag(workerTag)
                .setInputData(data.build())
                .build()
            workManager.enqueueUniqueWork(
                workerTag,
                ExistingWorkPolicy.REPLACE,
                oneTimeWork,
            )
        }
    }
}