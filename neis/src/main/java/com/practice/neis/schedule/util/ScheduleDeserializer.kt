package com.practice.neis.schedule.util

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.practice.neis.common.NeisDeserializer
import com.practice.neis.common.getInt
import com.practice.neis.common.getString
import com.practice.neis.common.pojo.Header
import com.practice.neis.schedule.pojo.ScheduleModel
import com.practice.neis.schedule.pojo.ScheduleResponseModel

class ScheduleDeserializer : NeisDeserializer<ScheduleModel, ScheduleResponseModel> {
    override val dataKey: String = "SchoolSchedule"
    override fun createResult(header: Header, responses: List<ScheduleModel>) =
        ScheduleResponseModel(header, responses)

    override fun throwException(message: String) = ScheduleDeserializerException(message)

    override fun parseData(jsonObj: JsonObject): List<ScheduleModel> {
        val rows = jsonObj.get("row").asJsonArray
        return rows.map { row ->
            with(row.asJsonObject) {
                ScheduleModel(
                    officeCode = getString("ATPT_OFCDC_SC_CODE"),
                    officeName = getString("ATPT_OFCDC_SC_NM"),
                    schoolCode = getInt("SD_SCHUL_CODE"),
                    schoolName = getString("SCHUL_NM"),
                    year = getInt("AY"),
                    dayNightName = parseNullString(get("DGHT_CRSE_SC_NM")),
                    schoolCourseName = getString("SCHUL_CRSE_SC_NM"),
                    notSchoolDayName = getString("SBTR_DD_SC_NM"),
                    yearMonthDay = getString("AA_YMD"),
                    eventName = getString("EVENT_NM"),
                    eventContent = parseNullString(get("EVENT_CNTNT")),
                    oneGradeEvent = getString("ONE_GRADE_EVENT_YN"),
                    twoGradeEvent = getString("TW_GRADE_EVENT_YN"),
                    threeGradeEvent = getString("THREE_GRADE_EVENT_YN"),
                    fourGradeEvent = getString("FR_GRADE_EVENT_YN"),
                    fiveGradeEvent = getString("FIV_GRADE_EVENT_YN"),
                    sixGradeEvent = getString("SIX_GRADE_EVENT_YN"),
                    loadDate = getString("LOAD_DTM"),
                )
            }
        }
    }

    private fun parseNullString(element: JsonElement): String? {
        return if (element is JsonNull) null else element.asString
    }
}

class ScheduleDeserializerException(override val message: String) : Exception(message)