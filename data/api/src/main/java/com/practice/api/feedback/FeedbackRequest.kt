package com.practice.api.feedback

data class FeedbackRequest(
    val userId: String,
    val feedback: String,
    val appVersionName: String,
)
