package com.practice.domain

data class School(
    val name: String,
    val schoolCode: Int,
) {
    companion object {
        val EmptySchool = School(
            name = "",
            schoolCode = -1,
        )
    }
}