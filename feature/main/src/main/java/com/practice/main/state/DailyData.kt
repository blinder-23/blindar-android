package com.practice.main.state

import com.hsk.ktx.date.Date
import com.practice.util.date.DateUtil
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

data class DailyData(
    val schoolCode: Int,
    val date: Date,
    val uiMeals: UiMeals,
    val uiSchedules: UiSchedules,
    val uiMemos: UiMemos,
) : Comparable<DailyData> {
    override fun compareTo(other: DailyData): Int {
        return date.compareTo(other.date)
    }

    val memoPopupElements: ImmutableList<MemoPopupElement>
        get() = mergeSchedulesAndMemos(uiSchedules, uiMemos)

    companion object {
        private const val EMPTY_SCHOOL_CODE = -1

        val sample = DailyData(
            schoolCode = EMPTY_SCHOOL_CODE,
            date = Date(2022, 10, 11),
            uiMeals = UiMeals(
                UiMeal(
                    year = 2022,
                    month = 10,
                    day = 11,
                    mealTime = "중식",
                    menus = (1..6).map { Menu("식단 $it") }.toPersistentList(),
                    nutrients = (0..3).map { Nutrient("탄수화물", 123.0, "g") }.toImmutableList()
                ),
            ),
            uiSchedules = UiSchedules(
                date = Date(2022, 10, 11),
                uiSchedules = (1..3).map {
                    UiSchedule(
                        schoolCode = EMPTY_SCHOOL_CODE,
                        year = 2022,
                        month = 10,
                        day = it,
                        id = it.toLong(),
                        eventName = "제목 $it",
                        eventContent = "학사일정 $it",
                    )
                }.toPersistentList(),
            ),
            uiMemos = UiMemos(
                date = Date(2022, 10, 11),
                memos = (1..3).map {
                    UiMemo(
                        id = it.toString(),
                        userId = "blindar",
                        year = 2022,
                        month = 10,
                        day = 11,
                        contents = "memo $it",
                        isSavedOnRemote = false,
                    )
                }.toImmutableList()
            )
        )
        val Empty = DailyData(
            schoolCode = EMPTY_SCHOOL_CODE,
            date = DateUtil.today(),
            uiMeals = UiMeals(),
            uiSchedules = UiSchedules.EmptyUiSchedules,
            uiMemos = UiMemos.EmptyUiMemos,
        )
    }
}