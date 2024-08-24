package com.practice.username

object NameValidator {
    private val regex = Regex("^[0-9a-zA-Z가-힣]+$")
    fun validate(name: String): Boolean {
        if (!regex.containsMatchIn(name)) return false
        val bytes = name.map { if (it.isDigit() || it.isAlphabet()) 1 else 2 }.sum()
        return bytes <= 30
    }

    private fun Char.isAlphabet() = this in 'A'..'z'
}