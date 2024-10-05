package com.practice.api.school.pojo

import com.google.gson.annotations.SerializedName

data class SchoolsResponse(
    @SerializedName("is_success") val isSuccess: Boolean,
    val response: List<SchoolModel>,
)
