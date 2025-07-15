package com.practice.api.user

import com.practice.domain.School
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteUserRepository(
    private val dataSource: RemoteUserDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun updateUserInfo(userId: String, schoolCode: Int, username: String) {
        withContext(ioDispatcher) {
            dataSource.updateUser(userId, schoolCode, username)
        }
    }

    suspend fun isUsernameDuplicate(username: String): Boolean {
        return dataSource.checkUsernameDuplication(username).isDuplicate
    }

    suspend fun isRegisterCompleted(userId: String): Boolean {
        return dataSource.checkUserRegisterCompleted(userId).isRegisterComplete
    }

    suspend fun getUserSchool(userId: String): School {
        return dataSource.getUserSchool(userId).school.run {
            School(
                name = schoolName,
                schoolCode = schoolCode,
            )
        }
    }
}