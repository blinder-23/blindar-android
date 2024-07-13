package com.practice.main.main

import android.content.Context
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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
    private val selectedYearMonth: Flow<YearMonth> = selectedDate.map {
        it.yearMonth
    }.distinctUntilChanged()

    private val selectedMealIndex = MutableStateFlow(0)

    private val isDialogVisible = MutableStateFlow(DialogUiState.EMPTY)

    private var loadMonthlyDataJob: Job? = null
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            collectMonthlyDataParameters()
        }
    }

    private suspend fun collectMonthlyDataParameters() {
        preferencesState.combine(selectedYearMonth) { preferencesState, selectedYearMonth ->
            MonthlyDataParameter(preferencesState.selectedSchool.schoolCode, selectedYearMonth)
        }.collectLatest(::collectMonthlyData)
    }

    private suspend fun collectMonthlyData(parameter: MonthlyDataParameter) {
        loadMonthlyDataJob?.cancel()

        val (schoolCode, yearMonth) = parameter
        val (year, month) = yearMonth
        loadMonthlyDataJob = viewModelScope.launch(Dispatchers.IO) {
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
            }.cancellable().distinctUntilChanged { old, new ->
                old.compareSizes(new)
            }.collectLatest {
                monthlyData.value = it
            }
        }
    }

    fun onDateClick(clickedDate: Date) {
        selectedDate.update { clickedDate }
        selectedMealIndex.update { 0 }
    }

    fun onRefreshIconClick(context: Context) {
        BlindarWorkManager.setOneTimeFetchDataWork(
            context = context,
            clearMealDatabase = true,
            clearScheduleDatabase = true,
        )
    }

    fun onMealTimeClick(index: Int) {
        selectedMealIndex.update { index }
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

    fun onSwiped(yearMonth: YearMonth) {
        val firstWeekday = yearMonth.getFirstWeekday()
        selectedDate.update { firstWeekday }
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

private data class MonthlyDataParameter(
    val schoolCode: Int,
    val yearMonth: YearMonth,
)

private fun MonthlyData.compareSizes(other: MonthlyData): Boolean {
    return this.year == other.year &&
            this.month == other.month &&
            this.meals.size == other.meals.size &&
            this.schedules.size == other.schedules.size &&
            this.memos.size == other.memos.size
}

private fun MonthlyData.getMeals(date: Date): UiMeals {
    val meals = meals.filter { it.dateEquals(date) }.map { it.toMealUiState() }
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