package com.practice.neis.schedule.util

import com.practice.neis.common.toJson
import com.practice.neis.schedule.pojo.ScheduleModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScheduleDeserializerTest {

    private val deserializer = ScheduleDeserializer()

    @Test
    fun parseSchedule() {
        val message = """
            {"row":[{
            "ATPT_OFCDC_SC_CODE":"B10",
            "ATPT_OFCDC_SC_NM":"서울특별시교육청",
            "SD_SCHUL_CODE":"7010578",
            "SCHUL_NM":"한빛맹학교",
            "AY":"2022",
            "DGHT_CRSE_SC_NM":null,
            "SCHUL_CRSE_SC_NM":"고등학교",
            "SBTR_DD_SC_NM":"공휴일",
            "AA_YMD":"20220505",
            "EVENT_NM":"어린이날",
            "EVENT_CNTNT":null,
            "ONE_GRADE_EVENT_YN":"Y",
            "TW_GRADE_EVENT_YN":"Y",
            "THREE_GRADE_EVENT_YN":"Y",
            "FR_GRADE_EVENT_YN":"N",
            "FIV_GRADE_EVENT_YN":"N",
            "SIX_GRADE_EVENT_YN":"N",
            "LOAD_DTM":"20220605"}]}
            """.trimIndent()
        val expected = listOf(
            ScheduleModel(
                officeCode = "B10",
                officeName = "서울특별시교육청",
                schoolCode = 7010578,
                schoolName = "한빛맹학교",
                year = 2022,
                dayNightName = null,
                schoolCourseName = "고등학교",
                notSchoolDayName = "공휴일",
                yearMonthDay = "20220505",
                eventName = "어린이날",
                eventContent = null,
                oneGradeEvent = "Y",
                twoGradeEvent = "Y",
                threeGradeEvent = "Y",
                fourGradeEvent = "N",
                fiveGradeEvent = "N",
                sixGradeEvent = "N",
                loadDate = "20220605"
            )
        )

        val actual = deserializer.parseData(message.toJson())
        assertEquals(expected, actual)
    }

}