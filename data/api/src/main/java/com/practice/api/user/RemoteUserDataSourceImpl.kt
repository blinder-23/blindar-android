package com.practice.api.user

import com.practice.api.user.pojo.RegisterCompleteResponse
import com.practice.api.user.pojo.RemoteUser
import com.practice.api.user.pojo.UserSchoolResponse
import com.practice.api.user.pojo.UserUpdateResponse
import com.practice.api.user.pojo.UsernameDuplicateResponse
import javax.inject.Inject

class RemoteUserDataSourceImpl @Inject constructor(private val api: UserApi): RemoteUserDataSource {
    override suspend fun updateUser(
        userId: String,
        schoolCode: Int,
        username: String
    ): UserUpdateResponse {
        val requestBody = RemoteUser(userId, schoolCode, username)
        return api.updateUser(requestBody)
    }

    override suspend fun checkUsernameDuplication(username: String): UsernameDuplicateResponse {
        return api.checkUserDuplication(username)
    }

    override suspend fun checkUserRegisterCompleted(userId: String): RegisterCompleteResponse {
        return api.checkUserRegisterCompleted(userId)
    }

    override suspend fun getUserSchool(userId: String): UserSchoolResponse {
        return api.getUserSchool(userId)
    }
}