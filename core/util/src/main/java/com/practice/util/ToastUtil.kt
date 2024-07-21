package com.practice.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.makeToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun Context.makeToast(@StringRes stringId: Int, duration: Int = Toast.LENGTH_LONG) {
    makeToast(getString(stringId), duration)
}