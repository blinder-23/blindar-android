package com.practice.neis.common

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

internal fun String.toJson(): JsonObject = JsonParser().parse(this).asJsonObject

internal fun JsonElement.getAsJsonObject(key: String): JsonElement = this.asJsonObject.get(key)

internal fun JsonObject.getString(key: String): String = get(key).asString

internal fun JsonObject.getInt(key: String): Int = get(key).asInt