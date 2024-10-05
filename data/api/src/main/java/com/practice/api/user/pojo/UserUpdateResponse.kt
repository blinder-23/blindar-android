package com.practice.api.user.pojo

import com.google.gson.annotations.SerializedName

data class UserUpdateResponse(
    @SerializedName("is_success") val isSuccess: Boolean,
    @SerializedName("response") val response: RemoteUser,
)