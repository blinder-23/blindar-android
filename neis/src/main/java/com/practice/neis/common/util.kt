package com.practice.neis.common

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

internal fun String.toJson(): JsonObject = JsonParser().parse(this).asJsonObject

fun JsonElement.getAsJsonObject(key: String): JsonElement = this.asJsonObject.get(key)