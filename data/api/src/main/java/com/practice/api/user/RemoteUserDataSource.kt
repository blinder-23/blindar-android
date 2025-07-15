package com.practice.api.user

import com.practice.api.user.pojo.RegisterCompleteResponse
import com.practice.api.user.pojo.UserSchoolResponse
import com.practice.api.user.pojo.UserUpdateResponse
import com.practice.api.user.pojo.UsernameDuplicateResponse

// TODO: 이거 필요없는 거 아닌지? RemoteUserRepository에 UserApi 바로 넣는게 맞지 않나
interface RemoteUserDataSource {
    suspend fun updateUser(userId: String, schoolCode: Int, username: String): UserUpdateResponse
    suspend fun checkUsernameDuplication(username: String): UsernameDuplicateResponse
    suspend fun checkUserRegisterCompleted(userId: String): RegisterCompleteResponse
    suspend fun getUserSchool(userId: String): UserSchoolResponse
}