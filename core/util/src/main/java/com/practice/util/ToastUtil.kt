package com.practice.util

import android.content.Context
import android.widget.Toast

// TODO: StringRes만 있어도 만들 수 있게 수정
fun Context.makeToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}