package com.practice.neis.common

import com.google.gson.*
import com.practice.neis.common.pojo.Header
import com.practice.neis.common.pojo.ResponseModel
import com.practice.neis.common.pojo.ResultCode
import java.lang.reflect.Type

interface NeisDeserializer<T, R : ResponseModel<T>> : JsonDeserializer<R> {
    val dataKey: String
    val createResult: (Header, List<T>) -> R
    val exception: (String) -> Exception

    fun parseData(jsonObj: JsonObject): List<T>

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): R {
        val jsonObjects = checkIfDataExists(json, dataKey).map { it.asJsonObject }

        val header = parseHeader(jsonObjects[0])
        val data = parseData(jsonObjects[1])
        return createResult(header, data)
    }

    /**
     * Returns the main data object if exists,
     * throw [NullPointerException] otherwise
     */
    fun checkIfDataExists(json: JsonElement?, dataKey: String): JsonArray {
        return json?.asJsonObject?.get(dataKey)?.asJsonArray
            ?: throwException(json)
    }

    private fun throwException(json: JsonElement?): JsonArray {
        val message = parseErrorMessage(json)
        throw exception(message)
    }

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