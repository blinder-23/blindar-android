package com.practice.notification.message

import com.hsk.ktx.date.Date
import com.practice.domain.meal.Meal

internal class MealMessageBuilder(private val date: Date, private val meals: List<Meal>) {
    val title: String
        get() = "${date.month}월 ${date.dayOfMonth}일 식단"

    val body: String
        get() = meals.joinToString("\n\n") { it.concatenatedMenu }
}