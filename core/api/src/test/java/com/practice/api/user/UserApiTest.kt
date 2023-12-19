package com.practice.api.user

import com.practice.api.TestRetrofit
import com.practice.api.user.pojo.UserUpdateRequestBody
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class UserApiTest {

    private lateinit var userApi: UserApi

    @BeforeEach
    fun setUp() {
        userApi = TestRetrofit.getRetrofit(UserApi::class.java)
    }

    @Test
    fun testUserUpdate() = runTest {
        val body = UserUpdateRequestBody(
            userId = "testUser",
            schoolCode = 100,
            username = "username"
        )
        assertDoesNotThrow {
            userApi.updateUser(body)
        }
    }
}