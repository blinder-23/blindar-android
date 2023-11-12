package com.practice.domain

// TODO: id를 String이 아니라 `MemoId` 클래스로 묶기? (value class)
data class Memo(
    val id: String,
    val userId: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String,
)
