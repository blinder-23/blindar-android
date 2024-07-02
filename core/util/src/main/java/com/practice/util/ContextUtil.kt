package com.practice.util

import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException

fun Context.getAppVersionName(): String {
    return try {
        // Deprecated되지 않은 다른 함수가 API 33 이상을 요구하여 불가피하게 deprecated 함수를 사용
        (packageManager.getPackageInfo(packageName, 0).versionName ?: "")
    } catch (e: NameNotFoundException) {
        ""
    }
}