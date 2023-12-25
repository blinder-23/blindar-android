package com.practice.api.feedback

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class FeedbackApiTest {
    private val api = feedbackApi

    @Test
    fun testFeedbackApi() = runTest {
        // TODO: 향후 테스트 서버 생기면 주석 해제하기
//        val requestBody = FeedbackRequestBody(
//            userId = "testId",
//            deviceName = "testDevice",
//            osVersion = "12",
//            appVersion = "2.1.0",
//            contents = "test feedback",
//        )
//        val result = api.sendFeedback(requestBody)
//        assertThat(result.isSuccessful).isTrue()
    }
}