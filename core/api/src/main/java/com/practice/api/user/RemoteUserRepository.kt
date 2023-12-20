package com.practice.api.user

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
}