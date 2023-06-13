package com.practice.util

import androidx.compose.runtime.MutableState

fun <T> MutableState<T>.update(block: T.() -> T) {
    val newValue = block(this.value)
    this.value = newValue
}