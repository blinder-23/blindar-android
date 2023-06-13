package com.practice.hanbitlunch

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class EmptyTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun emptyTest() {
        assert(true)
    }
}