package com.example.benchmark

import android.os.Build
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        BaselineProfileRule()
    } else {
        null
    }

    // Google API 없는 API 버전 33 이상 emulator에서 실행하자.
    // https://developer.android.com/topic/performance/baselineprofiles#creating-profile-rules

    // Baseline Profile을 생성할 때만 주석을 해제하자.
    // 평상시에는 androidTest에 영향을 주기 때문이다.
//    @Test
//    fun startUp() = rule?.collectBaselineProfile(packageName = "com.example.hanbitlunch") {
//        pressHome()
//        startActivityAndWait()
//    }
}