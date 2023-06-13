package com.practice.register

import com.practice.register.registerform.NameValidator
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class NameValidatorTest {

    @Test
    fun invalid_empty() {
        assertFalse(NameValidator.validate(""))
    }

    @Test
    fun invalid_specialCharacter() {
        assertFalse(NameValidator.validate("12a*"))
    }

    @Test
    fun invalid_tooLong() {
        assertFalse(NameValidator.validate("d".repeat(50)))
    }

    @Test
    fun invalid_tooLongByKorean() {
        assertFalse(NameValidator.validate("가".repeat(16)))
    }

    @Test
    fun valid_onlyAlphabet() {
        assert(NameValidator.validate("abcde"))
    }

    @Test
    fun valid_onlyKorean() {
        assert(NameValidator.validate("가나다라마"))
    }

    @Test
    fun valid_onlyKorean2() {
        assert(NameValidator.validate("가".repeat(15)))
    }

    @Test
    fun valid_digitsWithKorean() {
        assert(NameValidator.validate("가나다123"))
    }

    @Test
    fun valid_allType() {
        assert(NameValidator.validate("가나다abc123"))
    }
}