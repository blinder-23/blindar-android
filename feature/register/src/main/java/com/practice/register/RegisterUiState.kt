package com.practice.register

import com.practice.register.phonenumber.PhoneNumberValidator
import com.practice.register.registerform.NameValidator

data class RegisterUiState(
    val phoneNumber: String,
    val authCode: String,
    val isAuthCodeFieldEnabled: Boolean,
    val isVerifyCodeButtonEnabled: Boolean,
    val name: String,
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
        )
    }
}
