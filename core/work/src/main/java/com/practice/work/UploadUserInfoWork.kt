package com.practice.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.practice.api.user.RemoteUserRepository
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import com.practice.preferences.PreferencesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadUserInfoWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: RemoteUserRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val username = getUsername()
        val userId = getUserId()
        val schoolCode = getSchoolCode()
        val schoolName = getSchoolName()

        uploadToFirebase(username, schoolCode, schoolName)
        uploadToServer(userId, schoolCode, username)
        return Result.success()
    }

    private fun getUsername(): String? {
        val blindarUser = BlindarFirebase.getBlindarUser()
        return if (blindarUser is BlindarUserStatus.LoginUser) {
            blindarUser.user.displayName
        } else {
            null
        }
    }

    private fun getUserId(): String? {
        val blindarUser = BlindarFirebase.getBlindarUser()
        return if (blindarUser is BlindarUserStatus.LoginUser) {
            blindarUser.user.uid
        } else {
            null
        }
    }

    private fun getSchoolCode(): Int? {
        return preferencesRepository.userPreferencesFlow.value.let {
            if (it.isSchoolCodeEmpty) null else it.schoolCode
        }
    }

    private fun getSchoolName(): String? {
        return preferencesRepository.userPreferencesFlow.value.let {
            if (it.isSchoolNameEmpty) null else it.schoolName
        }
    }

    private fun uploadToFirebase(username: String?, schoolCode: Int?, schoolName: String?) {
        uploadUsernameToFirebase(username)
        uploadSchoolCodeToFirebase(schoolCode)
        uploadSchoolNameToFirebase(schoolName)
    }

    private fun uploadUsernameToFirebase(username: String?) {
        username?.let {
            BlindarFirebase.tryStoreUsername(
                username = username,
                onSuccess = {
                    log("upload username to firebase success")
                },
                onFail = {
                    log("upload username to firebase fail")
                },
            )
        } ?: run {
            logError("upload username to firebase fail: username is null")
        }
    }

    private fun uploadSchoolCodeToFirebase(schoolCode: Int?) {
        schoolCode?.let {
            BlindarFirebase.tryUpdateCurrentUserSchoolCode(
                schoolCode = schoolCode,
                onSuccess = {
                    log("upload school code to firebase success")
                },
                onFail = {
                    logError("upload school to firebase code fail")
                },
            )
        } ?: run {
            logError("upload school code to firebase fail: school code is null")
        }
    }

    private fun uploadSchoolNameToFirebase(schoolName: String?) {
        schoolName?.let {
            BlindarFirebase.tryUpdateCurrentUserSchoolName(
                schoolName = schoolName,
                onSuccess = { log("upload school name to firebase success") },
                onFail = { logError("upload school name to firebase fail") },
            )
        } ?: run {
            logError("upload school name to firebase fail: school name is null")
        }
    }

    private suspend fun uploadToServer(userId: String?, schoolCode: Int?, username: String?) {
        if (userId != null && schoolCode != null && username != null) {
            userRepository.updateUserInfo(userId, schoolCode, username)
            log("upload user info to server success: $userId, $schoolCode, $username")
        } else {
            logError("upload user into to server fail: $userId, $schoolCode, $username")
        }
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    private fun logError(message: String) {
        Log.e(TAG, message)
    }

    companion object {
        private const val TAG = "UploadUserInfoWork"
        private const val workerTag = "upload_user_info_to_firebase"

        fun setOneTimeWork(context: Context) {
            val workManager = WorkManager.getInstance(context)
            val oneTimeWork = OneTimeWorkRequestBuilder<UploadUserInfoWork>()
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