package com.practice.api.schedule.pojo

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ScheduleDeserializer : JsonDeserializer<ScheduleResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ScheduleResponse {
        val response = json?.asJsonObject?.get("response")?.asJsonArray
            ?: throw ScheduleDeserializerException("json is null.")
        return deserializeSchedules(response)
    }

    fun deserializeSchedules(response: JsonArray): ScheduleResponse {
        val schedules = response.map {
            val element = it.asJsonObject
            ScheduleModel(
                schoolCode = element.get("school_code").asInt,
                id = element.get("id").asLong,
                date = element.get("date").asLong,
                title = element.get("schedule").asString,
                contents = element.get("contents").asString,
            )
        }
        return ScheduleResponse(schedules)
    }
}

class ScheduleDeserializerException(override val message: String?) : Exception(message)