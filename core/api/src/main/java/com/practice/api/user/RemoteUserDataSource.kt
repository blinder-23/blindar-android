package com.practice.api.user

import com.practice.api.user.pojo.UserUpdateResponse

interface RemoteUserDataSource {
    suspend fun updateUser(userId: String, schoolCode: Int, username: String): UserUpdateResponse
}