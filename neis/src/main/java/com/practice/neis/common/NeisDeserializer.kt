package com.practice.neis.common

import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.practice.neis.common.pojo.Header
import com.practice.neis.common.pojo.ResultCode
import com.practice.neis.meal.util.getAsJsonObject

interface NeisDeserializer<T> : JsonDeserializer<T> {
    fun parseHeader(headJson: JsonObject): Header {
        val heads = headJson.get("head").asJsonArray

        val listTotalCount = heads[0].getAsJsonObject("list_total_count").asInt

        val resultObj = heads[1].getAsJsonObject("RESULT").asJsonObject
        val resultCodeObj = parseResultCode(resultObj)

        return Header(
            listTotalCount = listTotalCount,
            resultCode = resultCodeObj
        )
    }

    fun parseResultCode(jsonObj: JsonObject): ResultCode {
        val (type, code) = jsonObj.get("CODE").asString.split("-")
        val message = jsonObj.get("MESSAGE").asString

        return ResultCode(
            type = type,
            code = code,
            message = message
        )
    }

    fun parseErrorMessage(json: JsonElement?): String {
        val jsonObj = json?.getAsJsonObject("RESULT")?.asJsonObject
            ?: return "Json is null"
        return parseResultCode(jsonObj).message
    }
}