package com.practice.main.daily.components

import androidx.annotation.StringRes
import com.practice.main.R

enum class DateQuickNavigation(
    val daysToMove: Int,
    @StringRes val nameId: Int,
    @StringRes val descriptionId: Int,
) {
//    ONE_WEEK_BEFORE(
//        -7,
//        R.string.quick_navigation_minus_7,
//        R.string.quick_navigation_minus_7_description,
//    ),
    YESTERDAY(
        -1,
        R.string.quick_navigation_minus_1,
        R.string.quick_navigation_minus_1_description,
    ),
    TODAY(
        0,
        R.string.quick_navigation_today,
        R.string.quick_navigation_today_description,
    ),
    TOMORROW(
        1,
        R.string.quick_navigation_plus_1,
        R.string.quick_navigation_plus_1_description,
    ),
//    ONE_WEEK_AFTER(
//        7,
//        R.string.quick_navigation_plus_7,
//        R.string.quick_navigation_plus_7_description,
//    ),
}