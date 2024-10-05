package com.practice.api.user

import com.practice.api.user.pojo.RemoteUser
import com.practice.api.user.pojo.UserUpdateResponse
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
}