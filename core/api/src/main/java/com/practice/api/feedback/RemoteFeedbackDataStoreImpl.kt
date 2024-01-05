package com.practice.api.feedback

import android.util.Log
import com.practice.api.feedback.pojo.FeedbackRequestBody
import javax.inject.Inject

class RemoteFeedbackDataStoreImpl @Inject constructor(private val api: FeedbackApi) : RemoteFeedbackDataStore {
    override suspend fun sendFeedback(
        userId: String,
        deviceName: String,
        osVersion: String,
        appVersion: String,
        contents: String
    ): Int? {
        val body = FeedbackRequestBody(userId, deviceName, osVersion, appVersion, contents)
        return try {
            val response = api.sendFeedback(body)
            val code = response.code()
            if (response.isSuccessful) {
                log("Send feedback successfully!")
            } else {
                logError("Feedback error: code $code")
            }
            code
        } catch (e: Exception) {
            logError("api error: " + e.message)
            e.printStackTrace()
            null
        }
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    private fun logError(errorMessage: String) {
        Log.e(TAG, errorMessage)
    }

    companion object {
        private const val TAG = "FeedbackDataStoreImpl"
    }
}