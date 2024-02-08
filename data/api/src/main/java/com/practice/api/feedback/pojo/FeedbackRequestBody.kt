package com.practice.api.feedback.pojo

import com.google.gson.annotations.SerializedName

data class FeedbackRequestBody(
    @SerializedName("user_id") val userId: String,
    @SerializedName("device_name") val deviceName: String,
    @SerializedName("os_version") val osVersion: String,
    @SerializedName("app_version") val appVersion: String,
    @SerializedName("contents") val contents: String,
)