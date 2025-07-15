package com.practice.api.user.pojo

import com.google.gson.annotations.SerializedName

data class UsernameDuplicateResponse(
    @SerializedName("is_success") val isSuccess: Boolean,
    @SerializedName("response") val isDuplicate: Boolean,
)
