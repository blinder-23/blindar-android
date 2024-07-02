package com.practice.api.feedback

import com.practice.api.feedback.pojo.FeedbackRequestBody
import javax.inject.Inject

class RemoteFeedbackDataStoreImpl @Inject constructor(private val api: FeedbackApi) :
    RemoteFeedbackDataStore {
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
            response.code()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val TAG = "FeedbackDataStoreImpl"
    }
}