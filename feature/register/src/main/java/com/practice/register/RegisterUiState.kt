package com.practice.register

import com.practice.domain.School
import com.practice.register.phonenumber.PhoneNumberValidator
import com.practice.register.registerform.NameValidator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RegisterUiState(
    val phoneNumber: String,
    val authCode: String,
    val isAuthCodeFieldEnabled: Boolean,
    val isVerifyCodeButtonEnabled: Boolean,
    val name: String,
    val isDuplicateName: Boolean,
    val selectedSchool: School,
    val schoolQuery: String,
    val schools: ImmutableList<School>,
) {
    val isPhoneNumberValid: Boolean
        get() = PhoneNumberValidator.validate(phoneNumber)

    val isNameValid: Boolean
        get() = NameValidator.validate(name)

    companion object {
        val Empty = RegisterUiState(
            phoneNumber = "",
            authCode = "",
            isAuthCodeFieldEnabled = false,
            isVerifyCodeButtonEnabled = false,
            name = "",
            isDuplicateName = false,
            selectedSchool = School.EmptySchool,
            schoolQuery = "",
            schools = persistentListOf(),
        )
    }
}
