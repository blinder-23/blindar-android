package com.practice.api.school.pojo

data class SchoolsResponse(
    val message: String,
    val responseCode: Int,
    val data: List<SchoolModel>,
)
