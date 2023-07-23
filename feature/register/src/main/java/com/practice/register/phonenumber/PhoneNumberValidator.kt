package com.practice.register.phonenumber

object PhoneNumberValidator {
    private val regex = Regex("^01(0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$")
    fun validate(phoneNumber: String): Boolean {
        return regex.containsMatchIn(phoneNumber)
    }

    fun filterOnlyDigits(value: String): String {
        return value.filter { it.isDigit() }
    }
}