package com.practice.neis.schedule.util

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.practice.neis.common.NeisDeserializer
import com.practice.neis.common.pojo.Header
import com.practice.neis.schedule.pojo.ScheduleModel
import com.practice.neis.schedule.pojo.ScheduleResponseModel

class ScheduleDeserializer : NeisDeserializer<ScheduleModel, ScheduleResponseModel> {
    override val dataKey: String = "SchoolSchedule"
    override val createResult: (Header, List<ScheduleModel>) -> ScheduleResponseModel =
        ::ScheduleResponseModel
    override val exception: (String) -> Exception = ::ScheduleDeserializerException

    override fun parseData(jsonObj: JsonObject): List<ScheduleModel> {
        val rows = jsonObj.get("row").asJsonArray
        return rows.map { row ->
            with(row.asJsonObject) {
                ScheduleModel(
                    officeCode = get("ATPT_OFCDC_SC_CODE").asString,
                    officeName = get("ATPT_OFCDC_SC_NM").asString,
                    schoolCode = get("SD_SCHUL_CODE").asInt,
                    schoolName = get("SCHUL_NM").asString,
                    year = get("AY").asInt,
                    dayNightName = parseNullString(get("DGHT_CRSE_SC_NM")),
                    schoolCourseName = get("SCHUL_CRSE_SC_NM").asString,
                    notSchoolDayName = get("SBTR_DD_SC_NM").asString,
                    yearMonthDay = get("AA_YMD").asString,
                    eventName = get("EVENT_NM").asString,
                    eventContent = parseNullString(get("EVENT_CNTNT")),
                    oneGradeEvent = get("ONE_GRADE_EVENT_YN").asString,
                    twoGradeEvent = get("TW_GRADE_EVENT_YN").asString,
                    threeGradeEvent = get("THREE_GRADE_EVENT_YN").asString,
                    fourGradeEvent = get("FR_GRADE_EVENT_YN").asString,
                    fiveGradeEvent = get("FIV_GRADE_EVENT_YN").asString,
                    sixGradeEvent = get("SIX_GRADE_EVENT_YN").asString,
                    loadYearDateMonth = get("LOAD_DTM").asString,
                )
            }
        }
    }

    private fun parseNullString(element: JsonElement): String? {
        return if (element is JsonNull) null else element.asString
    }
}

class ScheduleDeserializerException(override val message: String) : Exception(message)