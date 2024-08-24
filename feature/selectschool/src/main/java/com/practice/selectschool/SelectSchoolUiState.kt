package com.practice.selectschool

import com.practice.domain.School
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SelectSchoolUiState(
    val selectedSchool: School,
    val schoolQuery: String,
    val schools: ImmutableList<School>,
) {

    companion object {
        val Empty = SelectSchoolUiState(
            selectedSchool = School.EmptySchool,
            schoolQuery = "",
            schools = persistentListOf(),
        )
    }
}
