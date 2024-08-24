package com.practice.register

import com.practice.domain.School
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RegisterUiState(
    val selectedSchool: School,
    val schoolQuery: String,
    val schools: ImmutableList<School>,
) {

    companion object {
        val Empty = RegisterUiState(
            selectedSchool = School.EmptySchool,
            schoolQuery = "",
            schools = persistentListOf(),
        )
    }
}
