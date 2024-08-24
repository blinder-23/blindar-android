package com.practice.register

import com.practice.domain.School
import com.practice.register.registerform.NameValidator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RegisterUiState(
    val name: String,
    val isDuplicateName: Boolean,
    val selectedSchool: School,
    val schoolQuery: String,
    val schools: ImmutableList<School>,
) {
    val isNameValid: Boolean
        get() = NameValidator.validate(name)

    companion object {
        val Empty = RegisterUiState(
            name = "",
            isDuplicateName = false,
            selectedSchool = School.EmptySchool,
            schoolQuery = "",
            schools = persistentListOf(),
        )
    }
}
