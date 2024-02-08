package com.practice.api.feedback

import com.practice.api.feedback.repository.FeedbackResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteFeedbackRepository(
    private val dataStore: RemoteFeedbackDataStore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
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