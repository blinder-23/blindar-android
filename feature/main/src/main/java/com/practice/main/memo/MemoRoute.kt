package com.practice.main.memo

import com.hsk.ktx.date.Date
import kotlinx.serialization.Serializable

@Serializable
data class MemoRoute(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val schoolCode: Int,
    val userId: String,
) {
    val date: Date
        get() = Date(year, month, dayOfMonth)
}