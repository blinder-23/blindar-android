package com.practice.api.user

import com.practice.api.TestRetrofit
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserApiTest {

    private lateinit var userApi: UserApi

    @BeforeEach
    fun setUp() {
        userApi = TestRetrofit.getRetrofit(UserApi::class.java)
    }

    @Test
    fun testUserUpdate() = runTest {
        // TODO: 향후 테스트 서버 생기면 주석 해제하기
//        val body = UserUpdateRequestBody(
//            userId = "testUser",
//            schoolCode = 100,
//            username = "username"
//        )
//        assertDoesNotThrow {
//            userApi.updateUser(body)
//        }
    }
}