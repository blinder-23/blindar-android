package com.practice.main.main.state

import kotlinx.serialization.Serializable

@Serializable
data class UiNutrient(
    val name: String,
    val amount: Double,
    val unit: String,
) {
    val description: String
        get() = "$name $amount $unit"
}