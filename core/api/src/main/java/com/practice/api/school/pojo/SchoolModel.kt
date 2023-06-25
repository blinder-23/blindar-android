package com.practice.api.school.pojo

import com.google.gson.annotations.SerializedName

data class SchoolModel(
    val name: String,
    @SerializedName("code") val schoolId: Int,
)
