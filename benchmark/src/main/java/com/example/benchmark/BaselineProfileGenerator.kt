package com.example.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalBaselineProfilesApi
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    // Google API 없는 emulator에서 실행하자.
    // https://developer.android.com/topic/performance/baselineprofiles#creating-profile-rules
    @Test
    fun startUp() = rule.collectBaselineProfile(packageName = "com.example.hanbitlunch") {
        pressHome()
        startActivityAndWait()
    }
}