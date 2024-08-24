package com.practice.verifyphone

data class VerifyPhoneUiState(
    val phoneNumber: String,
    val authCode: String,
    val isAuthCodeInvalid: Boolean,
    val isAuthCodeFieldEnabled: Boolean,
    val isVerifyCodeButtonEnabled: Boolean,
) {
    val isPhoneNumberValid: Boolean
        get() = PhoneNumberValidator.validate(phoneNumber)

    companion object {
        val Empty = VerifyPhoneUiState(
            phoneNumber = "",
            authCode = "",
            isAuthCodeInvalid = false,
            isAuthCodeFieldEnabled = false,
            isVerifyCodeButtonEnabled = false,
        )
    }
}