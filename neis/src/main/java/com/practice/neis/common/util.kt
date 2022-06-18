package com.practice.neis.common

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

internal fun getYearMonthString(year: Int, month: Int): String {
    val formattedMonth = month.toString().padStart(2, '0') // 01, 02, ...
    return "$year$formattedMonth"
}

internal fun String.toJson(): JsonObject = JsonParser().parse(this).asJsonObject

fun JsonElement.getAsJsonObject(key: String): JsonElement = this.asJsonObject.get(key)