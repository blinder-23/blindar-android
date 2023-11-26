package com.practice.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import com.practice.preferences.PreferencesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadUserInfoToFirebaseWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val preferencesRepository: PreferencesRepository,
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        uploadSchoolIdToFirebase()
        return Result.success()
    }
    // TODO: work에 analytics event 넣기
    private fun uploadUserIdToFirebaseDatabase() {
        val blindarUser = BlindarFirebase.getBlindarUser()
        if (blindarUser is BlindarUserStatus.LoginUser) {
            blindarUser.user.displayName?.let { username ->
                BlindarFirebase.tryStoreUsername(
                    username = username,
                    onSuccess = {
                        Log.d(TAG, "upload user id success")
                    },
                    onFail = {
                        Log.d(TAG, "upload user id fail")
                    },
                )
            }
        }
    }

    private fun uploadSchoolIdToFirebase() {
        val schoolCode = preferencesRepository.userPreferencesFlow.value.schoolCode
        BlindarFirebase.tryUpdateCurrentUserSchoolCode(
            schoolCode = schoolCode,
            onSuccess = {
                Log.d(TAG, "upload school id success")
            },
            onFail = {
                Log.d(TAG, "upload school id fail")
            },
        )
    }

    companion object {
        private const val TAG = "UploadUserInfoWork"
        private const val workerTag = "upload_user_info_to_firebase"

        fun setOneTimeWork(context: Context) {
            val workManager = WorkManager.getInstance(context)
            val oneTimeWork = OneTimeWorkRequestBuilder<UploadUserInfoToFirebaseWork>()
                .addTag(workerTag)
                .build()
            workManager.enqueueUniqueWork(
                workerTag,
                ExistingWorkPolicy.REPLACE,
                oneTimeWork,
            )
        }
    }
}