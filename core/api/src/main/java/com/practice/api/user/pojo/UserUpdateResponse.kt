package com.practice.api.user.pojo

import com.google.gson.annotations.SerializedName

data class UserUpdateResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("school_code") val schoolCode: Int,
    @SerializedName("name") val username: String,
)
