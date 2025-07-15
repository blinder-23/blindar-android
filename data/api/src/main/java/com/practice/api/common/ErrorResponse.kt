package com.practice.api.common

data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String,
)
