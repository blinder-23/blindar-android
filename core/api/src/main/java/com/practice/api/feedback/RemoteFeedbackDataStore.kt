package com.practice.api.feedback

interface RemoteFeedbackDataStore {
    suspend fun sendFeedback(
        userId: String,
        deviceName: String,
        osVersion: String,
        appVersion: String,
        contents: String,
    ): Int?
}