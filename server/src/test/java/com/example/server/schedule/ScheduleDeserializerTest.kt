package com.example.server.schedule

import com.example.server.schedule.pojo.ScheduleDeserializer
import com.example.server.schedule.pojo.ScheduleModel
import com.example.server.schedule.pojo.ScheduleResponse
import com.google.gson.JsonParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScheduleDeserializerTest {

    private val deserializer = ScheduleDeserializer()
    private val json = """
        {
            "response": [
                {
                    "id": 1149108,
                    "date": 1662044400,
                    "schedule": "봉사활동 사전 교육"
                },
                {
                    "id": 1149109,
                    "date": 1662303600,
                    "schedule": "초등부 '독서기록 꿈자람' 시작"
                }
            ]
        }
    """.trimIndent()
    private val answer = ScheduleResponse(
        listOf(
            ScheduleModel(
                id = 1149108,
                date = 1662044400L,
                title = "봉사활동 사전 교육"
            ),
            ScheduleModel(
                id = 1149109,
                date = 1662303600L,
                title = "초등부 '독서기록 꿈자람' 시작"
            )
        )
    )

    @Test
    fun `test by sample json`() {
        val jsonObj = JsonParser.parseString(json).asJsonObject
        val actual = deserializer.deserializeSchedules(jsonObj.get("response").asJsonArray)
        assertThat(actual).isEqualTo(answer)
    }

}