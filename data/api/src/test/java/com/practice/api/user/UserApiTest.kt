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

    @Test
    fun testUsernameDuplicate() = runTest {
        // TODO: 향후 테스트 서버 생기면 주석 해제하기
//        val username = "블린더"
//        assertDoesNotThrow {
//            userApi.checkUserDuplication(username)
//        }
    }

    @Test
    fun testUserRegisterCompleted() = runTest {
        // TODO: 향후 테스트 서버 생기면 주석 해제하기
//        val userId = "replace this with fake user id"
//        assertDoesNotThrow {
//            userApi.checkUserRegisterCompleted(userId)
//        }
    }
}