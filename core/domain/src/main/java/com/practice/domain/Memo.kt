package com.practice.domain

data class Memo(
    val id: String,
    val userId: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String,
)
