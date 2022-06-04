package com.practice.neis.meal.pojo

data class MealResultCode(
    val code: String,
    val message: String
) {
    companion object {
        const val successCode = "INFO-000"
    }
}