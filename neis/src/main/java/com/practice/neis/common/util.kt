package com.practice.neis.common

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.practice.neis.common.pojo.Header
import com.practice.neis.common.pojo.ResultCode
import com.practice.neis.meal.util.getAsJsonObject

internal fun getYearMonthString(year: Int, month: Int): String {
    val formattedMonth = month.toString().padStart(2, '0') // 01, 02, ...
    return "$year$formattedMonth"
}

internal fun parseHeader(headJson: JsonObject): Header {
    val heads = headJson.get("head").asJsonArray

    val listTotalCount = heads[0].getAsJsonObject("list_total_count").asInt

    val resultObj = heads[1].getAsJsonObject("RESULT").asJsonObject
    val resultCode = resultObj.get("CODE").asString
    val resultMessage = resultObj.get("MESSAGE").asString

    return Header(
        listTotalCount = listTotalCount,
        resultCode = ResultCode(
            code = resultCode,
            message = resultMessage
        )
    )
}

internal fun String.toJson(): JsonObject = JsonParser().parse(this).asJsonObject