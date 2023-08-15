package com.practice.register.selectschool

data class School(
    val name: String,
    val schoolId: Int,
) {
    companion object {
        val EmptySchool = School(
            name = "",
            schoolId = -1,
        )
    }
}