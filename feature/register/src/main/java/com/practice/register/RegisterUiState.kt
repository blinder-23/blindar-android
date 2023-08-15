package com.practice.register

import com.practice.register.phonenumber.PhoneNumberValidator
import com.practice.register.registerform.NameValidator
import com.practice.register.selectschool.School
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RegisterUiState(
    val phoneNumber: String,
    val authCode: String,
    val isAuthCodeFieldEnabled: Boolean,
    val isVerifyCodeButtonEnabled: Boolean,
    val name: String,
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
            selectedSchool = School.EmptySchool,
            schoolQuery = "",
            schools = persistentListOf(),
        )
    }
}
