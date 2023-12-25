package com.practice.api.feedback.repository

sealed class FeedbackResult {
    object Success: FeedbackResult()
    object Fail: FeedbackResult()
}