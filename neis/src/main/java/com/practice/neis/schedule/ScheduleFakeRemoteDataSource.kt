package com.practice.neis.schedule

import com.hsk.ktx.getDateString
import com.practice.neis.common.seoulOfficeCode
import com.practice.neis.schedule.pojo.ScheduleModel

class ScheduleFakeRemoteDataSource : ScheduleRemoteDataSource {
    private val schedules = (1..20).map {
        ScheduleModel(
            officeCode = seoulOfficeCode,
            officeName = "테스트교육청",
            schoolCode = 999,
            schoolName = "테스트학교",
            year = 2022,
            dayNightName = null,
            schoolCourseName = "일반",
            notSchoolDayName = "행사",
            yearMonthDay = getDateString(2022, 8, it),
            eventName = "테스트일정 $it",
            eventContent = null,
            oneGradeEvent = "일정 $it",
            twoGradeEvent = "일정 $it",
            threeGradeEvent = "일정 $it",
            fourGradeEvent = "일정 $it",
            fiveGradeEvent = "일정 $it",
            sixGradeEvent = "일정 $it",
            loadDate = getDateString(2022, 8, 1)
        )
    }

    override suspend fun getSchedules(year: Int, month: Int): List<ScheduleModel> {
        return schedules.filter {
            it.yearMonthDay.contains("$year${month.toString().padStart(2, '0')}")
        }
    }
}