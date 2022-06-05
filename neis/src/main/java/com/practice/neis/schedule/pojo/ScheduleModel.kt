package com.practice.neis.schedule.pojo

data class ScheduleModel(
    val officeCode: String,
    val officeName: String,
    val schoolCode: Int,
    val schoolName: String,
    val year: Int,
    val dayNightName: String?,
    val schoolCourseName: String,
    val notSchoolDayName: String,
    val yearMonthDay: String,
    val eventName: String,
    val eventContent: String?,
    val oneGradeEvent: String,
    val twoGradeEvent: String,
    val threeGradeEvent: String,
    val fourGradeEvent: String,
    val fiveGradeEvent: String,
    val sixGradeEvent: String,
    val loadYearDateMonth: String,
)

/*
{
    "ATPT_OFCDC_SC_CODE": "B10",
    "ATPT_OFCDC_SC_NM": "서울특별시교육청",
    "SD_SCHUL_CODE": "7010578",
    "SCHUL_NM": "한빛맹학교",
    "AY": "2022",
    "DGHT_CRSE_SC_NM": null,
    "SCHUL_CRSE_SC_NM": "고등학교",
    "SBTR_DD_SC_NM": "공휴일",
    "AA_YMD": "20220505",
    "EVENT_NM": "어린이날",
    "EVENT_CNTNT": null,
    "ONE_GRADE_EVENT_YN": "Y",
    "TW_GRADE_EVENT_YN": "Y",
    "THREE_GRADE_EVENT_YN": "Y",
    "FR_GRADE_EVENT_YN": "N",
    "FIV_GRADE_EVENT_YN": "N",
    "SIX_GRADE_EVENT_YN": "N",
    "LOAD_DTM": "20220604"
}
 */