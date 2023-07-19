package com.practice.register

import com.practice.register.phonenumber.PhoneNumberValidator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class PhoneNumberValidatorTest {
    /**
     * validate phone number test
     */

    @Test
    fun nonDigitsOnly() {
        assertFalse(PhoneNumberValidator.validate("abcd"))
    }

    @Test
    fun containsNonDigit() {
        assertFalse(PhoneNumberValidator.validate("123a"))
    }

    @Test
    fun lengthNotValid() {
        assertFalse(PhoneNumberValidator.validate("012345689"))
    }

    @Test
    fun prefixNotValid() {
        assertFalse(PhoneNumberValidator.validate("02134567890"))
    }

    @Test
    fun valid() {
        assert(PhoneNumberValidator.validate("01012345678"))
    }

    @Test
    fun invalidPrefix() {
        assertFalse(PhoneNumberValidator.validate("01422243368"))
    }

    @Test
    fun pastNumber_valid() {
        assert(PhoneNumberValidator.validate("0162192448"))
    }

    @Test
    fun pastNumber_invalidPrefix() {
        assertFalse(PhoneNumberValidator.validate("0143381687"))
    }

    @Test
    fun suffix_matches() {
        assertFalse(PhoneNumberValidator.validate("001012345678"))
    }

    /**
     * filter only digits test
     */
    @Test
    fun emptyString() {
        assertThat(PhoneNumberValidator.filterOnlyDigits("")).isEqualTo("")
    }

    @Test
    fun digitsString() {
        val string = "0123456789"
        assertThat(PhoneNumberValidator.filterOnlyDigits(string)).isEqualTo(string)
    }

    @Test
    fun asciiCharacterString() {
        val string = "012a345er67.;89"
        assertThat(PhoneNumberValidator.filterOnlyDigits(string)).isEqualTo("0123456789")
    }

    @Test
    fun unicodeCharacterString() {
        val string = "0123456가7닭89"
        assertThat(PhoneNumberValidator.filterOnlyDigits(string)).isEqualTo("0123456789")
    }
}