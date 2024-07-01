package com.practice.main.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsk.ktx.date.Date
import com.practice.combine.MonthlyData
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.designsystem.calendar.core.getFirstWeekday
import com.practice.designsystem.calendar.core.yearMonth
import com.practice.domain.Memo
import com.practice.domain.School
import com.practice.domain.meal.Meal
import com.practice.domain.schedule.Schedule
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import com.practice.main.main.state.DailyData
import com.practice.main.main.state.DialogUiState
import com.practice.main.main.state.MainUiMode.Calendar.toUiLoadedMode
import com.practice.main.main.state.MainUiState
import com.practice.main.main.state.PreferencesState
import com.practice.main.state.UiMeals
import com.practice.main.state.UiMemos
import com.practice.main.state.UiSchedules
import com.practice.main.state.toMealUiState
import com.practice.main.state.toUiMemo
import com.practice.main.state.toUiSchedule
import com.practice.meal.MealRepository
import com.practice.memo.MemoRepository
import com.practice.preferences.PreferencesRepository
import com.practice.schedule.ScheduleRepository
import com.practice.util.date.DateUtil
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val scheduleRepository: ScheduleRepository,
    private val memoRepository: MemoRepository,
    preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val userId = getCurrentUserId()

    private val preferencesState: Flow<PreferencesState> =
        preferencesRepository.userPreferencesFlow.map {
            if (initialWorkCount == null) {
                initialWorkCount = it.runningWorksCount
            }
            PreferencesState.Loaded(
                isRefreshing = it.runningWorksCount != initialWorkCount,
                selectedSchool = School(
                    name = it.schoolName,
                    schoolCode = it.schoolCode,
                ),
                mainUiMode = it.mainScreenMode.toUiLoadedMode(),
            )
        }
    private val selectedDate = MutableStateFlow(Date.now())
    private val selectedMealIndex = MutableStateFlow(0)
    private val isDialogVisible = MutableStateFlow(DialogUiState.EMPTY)
    private val monthlyData: MutableStateFlow<MonthlyData> = MutableStateFlow(MonthlyData.Empty)

    val uiState: StateFlow<MainUiState> = combine(
        preferencesState,
        selectedDate,
        selectedMealIndex,
        isDialogVisible,
        monthlyData,
    ) { preferencesState, selectedDate, selectedMealIndex, isDialogVisible, monthlyData ->
        when (preferencesState) {
            is PreferencesState.Loading -> {
                MainUiState.EMPTY
            }

            is PreferencesState.Loaded -> {
                MainUiState(
                    userId = userId,
                    selectedDate = selectedDate,
                    monthlyDataState = parseDailyState(monthlyData),
                    selectedMealIndex = selectedMealIndex,
                    isLoading = preferencesState.isRefreshing,
                    selectedSchool = preferencesState.selectedSchool,
                    isMealDialogVisible = isDialogVisible.isMealDialogVisible,
                    isScheduleDialogVisible = isDialogVisible.isScheduleDialogVisible,
                    mainUiMode = preferencesState.mainUiMode,
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, MainUiState.EMPTY)

    // For internal use only
    private val state: MainUiState
        get() = uiState.value

    private var initialWorkCount: Int? = null

    private fun getCurrentUserId(): String {
        return when (val currentlyLoggedInUser = BlindarFirebase.getBlindarUser()) {
            is BlindarUserStatus.LoginUser -> currentlyLoggedInUser.user.uid
            is BlindarUserStatus.NotLoggedIn -> ""
        }
    }

    private suspend fun collectPreferences() {
        preferencesState.collectLatest {
            when (it) {
                is PreferencesState.Loading -> MonthlyData.Empty
                is PreferencesState.Loaded -> {
                    collectMonthlyData(it)
                }
            }
        }
    }

    private suspend fun collectMonthlyData(preferences: PreferencesState.Loaded) {
        val schoolCode = preferences.selectedSchool.schoolCode
        val (year, month) = selectedDate.value.yearMonth

        combine(
            mealRepository.getMeals(schoolCode, year, month),
            scheduleRepository.getSchedules(schoolCode, year, month),
            memoRepository.getMemos(userId, year, month),
        ) { meals, schedules, memos ->
            MonthlyData(
                schoolCode = schoolCode,
                year = year,
                month = month,
                meals = meals.toImmutableList(),
                schedules = schedules.toImmutableList(),
                memos = memos.toImmutableList(),
            )
        }.collectLatest {
            monthlyData.value = it
        }
    }

    /**
     * init 블럭에서 실행하지 않은 이유는 [IllegalStateException]이 발생하기 때문이다.
     * 아직 UI에 반영되지 않은 값을 참조하기 때문에 예외가 발생한다.
     */
    fun onLaunch() {
        viewModelScope.launch {
            collectPreferences()
        }
    }

    fun onDateClick(clickedDate: Date) = viewModelScope.launch(Dispatchers.IO) {
        Log.d(TAG, "clicked date: $clickedDate")
        selectedDate.value = clickedDate
        selectedMealIndex.value = 0
    }

    fun onRefreshIconClick(context: Context) {
        BlindarWorkManager.setOneTimeFetchDataWork(
            context = context,
            clearMealDatabase = true,
            clearScheduleDatabase = true,
        )
    }

    fun onMealTimeClick(index: Int) {
        selectedMealIndex.value = index
    }

    private fun parseDailyState(monthlyData: MonthlyData): List<DailyData> {
        val allDates = mutableSetOf<Date>().apply {
            addAll(monthlyData.meals.map { Date(it.year, it.month, it.day) })
            addAll(monthlyData.schedules.map { Date(it.year, it.month, it.day) })
            addAll(monthlyData.memos.map { Date(it.year, it.month, it.day) })
        }
        val newDailyData = allDates.map { date ->
            val uiMeals = monthlyData.getMeals(date)
            val uiSchedules = monthlyData.getSchedule(date)
            val uiMemos = monthlyData.getMemo(date)
            DailyData(
                schoolCode = monthlyData.schoolCode,
                date = date,
                uiMeals = uiMeals,
                uiSchedules = uiSchedules,
                uiMemos = uiMemos,
            )
        }.sorted()
        return newDailyData
    }

    fun onSwiped(yearMonth: YearMonth) = viewModelScope.launch {
        val firstWeekday = yearMonth.getFirstWeekday()
        selectedDate.value = firstWeekday
    }

    fun getContentDescription(date: Date): String {
        // TODO: dangerous???
        val dailyState = state.monthlyDataState.find { it.date == date }

        val isSelectedString = if (date == selectedDate.value) "선택됨" else ""
        val isTodayString = if (date == DateUtil.today()) "오늘" else ""
        val dailyStateString = if (dailyState != null) {
            "식단: ${dailyState.uiMeals.description}\n학사일정:${dailyState.uiSchedules.description}\n메모: ${dailyState.uiMemos.description}"
        } else {
            ""
        }

        return listOf(isSelectedString, isTodayString, dailyStateString)
            .filter { it.isNotEmpty() }
            .joinToString(", ")
    }

    fun getClickLabel(date: Date): String =
        if (date == selectedDate.value) "" else "식단 및 학사일정 보기"

    fun onMealDialog() {
        isDialogVisible.update {
            it.copy(isMealDialogVisible = true)
        }
    }

    fun onMealDialogClose() {
        isDialogVisible.update {
            it.copy(isMealDialogVisible = false)
        }
    }

    fun onScheduleDialogOpen() {
        isDialogVisible.update {
            it.copy(isScheduleDialogVisible = true)
        }
    }

    fun onScheduleDialogClose() {
        isDialogVisible.update {
            it.copy(isScheduleDialogVisible = false)
        }
    }

    companion object {
        private const val TAG = "MainScreenViewModel"
    }
}

private fun MonthlyData.getMeals(date: Date): UiMeals {
    val meals = meals.filter { it.dateEquals(date) }.map { it.toMealUiState() }
    Log.d("MainScreenModel", "$date: $meals")
    return UiMeals(meals)
}

private fun MonthlyData.getSchedule(date: Date): UiSchedules {
    return try {
        val uiSchedules = schedules.filter { it.dateEquals(date) }.map { it.toUiSchedule() }
        UiSchedules(
            date = date,
            uiSchedules = uiSchedules.toPersistentList(),
        )
    } catch (e: NoSuchElementException) {
        UiSchedules.EmptyUiSchedules
    }
}

private fun MonthlyData.getMemo(date: Date): UiMemos {
    return try {
        val memos = memos.filter { it.dateEquals(date) }.map { it.toUiMemo() }
        UiMemos(
            date = date,
            memos = memos.toPersistentList(),
        )
    } catch (e: NoSuchElementException) {
        UiMemos.EmptyUiMemos
    }
}

private fun Meal.dateEquals(date: Date) =
    this.year == date.year && this.month == date.month && this.day == date.dayOfMonth

private fun Schedule.dateEquals(date: Date) =
    this.year == date.year && this.month == date.month && this.day == date.dayOfMonth

private fun Memo.dateEquals(date: Date) =
    this.year == date.year && this.month == date.month && this.day == date.dayOfMonth