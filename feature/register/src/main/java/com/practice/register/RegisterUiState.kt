package com.practice.register

import com.practice.register.phonenumber.PhoneNumberValidator

data class RegisterUiState(
    val phoneNumber: String,
    val authCode: String,
    val isAuthCodeFieldEnabled: Boolean,
    val isVerifyCodeButtonEnabled: Boolean,
) {
    val isPhoneNumberValid: Boolean
        get() = PhoneNumberValidator.validate(phoneNumber)

    companion object {
        val Empty = RegisterUiState(
            phoneNumber = "",
            authCode = "",
            isAuthCodeFieldEnabled = false,
            isVerifyCodeButtonEnabled = false,
        )
    }
}
