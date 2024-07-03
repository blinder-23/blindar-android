package com.practice.api.feedback

import android.os.Build
import com.practice.api.feedback.repository.FeedbackResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteFeedbackRepository(
    private val dataStore: RemoteFeedbackDataStore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun sendFeedback(request: FeedbackRequest): FeedbackResult {
        return sendFeedback(
            userId = request.userId,
            deviceName = Build.MODEL,
            osVersion = Build.VERSION.SDK_INT.toString(),
            appVersion = request.appVersionName,
            contents = request.feedback,
        )
    }

    // TODO: 이거 private으로 전환하고, 사용자 id 얻는 과정 일원화하기
    suspend fun sendFeedback(
        userId: String,
        deviceName: String,
        osVersion: String,
        appVersion: String,
        contents: String,
    ): FeedbackResult {
        return withContext(ioDispatcher) {
            val code = dataStore.sendFeedback(userId, deviceName, osVersion, appVersion, contents)
            if (code in 200 until 300) {
                FeedbackResult.Success
            } else {
                FeedbackResult.Fail
            }
        }
    }
}