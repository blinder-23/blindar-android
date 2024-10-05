package com.practice.api.memo.pojo

import com.google.gson.annotations.SerializedName

data class UpdateMemoResponse(
    @SerializedName("is_success") val isSuccess: Boolean,
    val response: MemoIDResponse,
) {
    constructor(isSuccess: Boolean, id: String) : this(isSuccess, MemoIDResponse(id))
}