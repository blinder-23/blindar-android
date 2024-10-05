package com.practice.api.memo.pojo

import com.google.gson.annotations.SerializedName

data class MemoIDResponse(
    @SerializedName("memo_id") val id: String,
)
