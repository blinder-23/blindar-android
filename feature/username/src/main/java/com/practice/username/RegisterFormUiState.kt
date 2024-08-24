package com.practice.username

data class RegisterFormUiState(
    val name: String,
    val isDuplicateName: Boolean,
) {
    val isNameValid: Boolean
        get() = NameValidator.validate(name)

    companion object {
        val Empty = RegisterFormUiState(
            name = "",
            isDuplicateName = false,
        )
    }
}