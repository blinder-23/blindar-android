package com.practice.api.memo.pojo

import com.google.gson.annotations.SerializedName

data class GetMemoResponse(
    @SerializedName("response") val memos: List<RemoteMemoEntity>,
)
