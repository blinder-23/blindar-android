package com.practice.api.memo.pojo

import com.google.gson.annotations.SerializedName

data class RemoteMemoEntity(
    @SerializedName("user_id") val userId: String,
    val date: String,
    @SerializedName("memo_id") val id: String,
    val contents: String,
)
