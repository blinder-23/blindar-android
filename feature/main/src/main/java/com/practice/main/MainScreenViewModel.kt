package com.practice.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsk.ktx.date.Date
import com.practice.combine.LoadMonthlyDataUseCase
import com.practice.combine.MonthlyData
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.designsystem.calendar.core.getFirstWeekday
import com.practice.designsystem.calendar.core.yearMonth
import com.practice.domain.Memo
import com.practice.domain.School
import com.practice.domain.meal.Meal
import com.practice.domain.schedule.Schedule
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUser
import com.practice.main.state.DailyData
import com.practice.main.state.MainUiState
import com.practice.main.state.MealUiState
import com.practice.main.state.MemoUiState
import com.practice.main.state.ScheduleUiState
import com.practice.main.state.UiMemo
import com.practice.main.state.toMealUiState
import com.practice.main.state.toMemo
import com.practice.main.state.toUiMemo
import com.practice.main.state.toUiSchedule
import com.practice.preferences.PreferencesRepository
import com.practice.preferences.ScreenMode
import com.practice.util.date.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val loadMonthlyDataUseCase: LoadMonthlyDataUseCase,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val _uiState: MutableState<MainUiState>
    val uiState: State<MainUiState>
        get() = _uiState

    // For internal use only
    private var state: MainUiState
        get() = _uiState.value
        set(value) {
            _uiState.value = value
        }

    private val _scheduleDates: MutableStateFlow<Set<Date>>
    val scheduleDates: StateFlow<Set<Date>>
        get() = _scheduleDates

    private val selectedDateFlow: MutableStateFlow<Date>

    // TODO: domain 또는 data로 옮기기?
    private val cache: MutableMap<CacheKey, MonthlyData>

    init {
        val current = Date.now()
        val userId = getCurrentUserId()
        _uiState = mutableStateOf(MainUiState.EMPTY.copy(userId = userId))
        selectedDateFlow = MutableStateFlow(current)
        _scheduleDates = MutableStateFlow(emptySet())
        cache = mutableMapOf()
    }

    private fun getCurrentUserId(): String {
        return when (val currentlyLoggedInUser = BlindarFirebase.getBlindarUser()) {
            is BlindarUser.LoginUser -> currentlyLoggedInUser.user.uid
            is BlindarUser.NotLoggedIn -> ""
        }
    }

    /**
     * init 블럭에서 실행하지 않은 이유는 [IllegalStateException]이 발생하기 때문이다.
     * 아직 UI에 반영되지 않은 값을 참조하기 때문에 예외가 발생한다.
     */
    fun onLaunch() {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = loadMonthlyData(state.userId, state.selectedSchoolCode, state.yearMonth)
            updateUiState(entity = entity)
        }
        viewModelScope.launch(Dispatchers.IO) {
            collectPreferences()
        }
    }

    /**
     * Kotlin Flow의 combine 함수를 본따 작성했다.
     */
    private fun updateUiState(
        userId: String = state.userId,
        yearMonth: YearMonth = state.yearMonth,
        selectedDate: Date = state.selectedDate,
        entity: MonthlyData? = cache[state.cacheKey],
        isLoading: Boolean = state.isLoading,
        screenMode: ScreenMode = state.screenMode,
        selectedSchool: School = state.selectedSchool,
        isNutrientPopupVisible: Boolean = state.isNutrientPopupVisible,
        isMemoPopupVisible: Boolean = state.isMemoPopupVisible,
    ) {
        val monthlyMealScheduleState = if (entity != null) {
            parseDailyState(entity)
        } else {
            emptyList()
        }
        synchronized(state) {
            state = state.copy(
                userId = userId,
                year = yearMonth.year,
                month = yearMonth.month,
                monthlyDataState = monthlyMealScheduleState,
                selectedDate = selectedDate,
                isLoading = isLoading,
                screenMode = screenMode,
                selectedSchool = selectedSchool,
                isNutrientPopupVisible = isNutrientPopupVisible,
                isMemoPopupVisible = isMemoPopupVisible,
            )
        }
        entity?.let {
            updateScheduleDates(it)
        }
    }

    fun onScreenModeChange(screenMode: ScreenMode) =
        viewModelScope.launch {
            preferencesRepository.updateScreenMode(screenMode)
        }

    private fun updateScheduleDates(entity: MonthlyData) {
        _scheduleDates.value = scheduleDates.value.toMutableSet().apply {
            addAll(entity.schedules.map { Date(it.year, it.month, it.day) })
        }
    }

    fun onDateClick(clickedDate: Date) = viewModelScope.launch(Dispatchers.IO) {
        val entity = loadMonthlyData(state.userId, state.selectedSchoolCode, clickedDate.yearMonth)
        updateUiState(
            yearMonth = clickedDate.yearMonth,
            selectedDate = clickedDate,
            entity = entity
        )
    }

    suspend fun updateMemo(uiMemo: UiMemo) {
        loadMonthlyDataUseCase.updateMemo(uiMemo.toMemo())
    }

    suspend fun deleteMemo(uiMemo: UiMemo) {
        loadMonthlyDataUseCase.deleteMemo(uiMemo.toMemo())
    }

    private suspend fun loadMonthlyData(
        userId: String,
        schoolCode: Int,
        yearMonth: YearMonth
    ): MonthlyData {
        val cacheKey = CacheKey(schoolCode, yearMonth)
        return if (cache.containsKey(cacheKey)) {
            cache[cacheKey]!!
        } else {
            val (queryYear, queryMonth) = yearMonth
            loadMonthlyDataUseCase.loadData(userId, schoolCode, queryYear, queryMonth).first()
                .apply {
                    cache[cacheKey] = this
                }
        }
    }

    private fun parseDailyState(monthlyData: MonthlyData): List<DailyData> {
        val allDates = mutableSetOf<Date>().apply {
            addAll(monthlyData.meals.map { Date(it.year, it.month, it.day) })
            addAll(monthlyData.schedules.map { Date(it.year, it.month, it.day) })
            addAll(monthlyData.memos.map { Date(it.year, it.month, it.day) })
        }
        val newDailyData = allDates.map { date ->
            val meal = monthlyData.getMeal(date)
            val schedule = monthlyData.getSchedule(date)
            val memo = monthlyData.getMemo(date)
            DailyData(
                schoolCode = monthlyData.schoolCode,
                date = date,
                mealUiState = meal,
                scheduleUiState = schedule,
                memoUiState = memo,
            )
        }.sorted()
        return newDailyData
    }

    fun onSwiped(yearMonth: YearMonth) = viewModelScope.launch {
        val entity = loadMonthlyData(state.userId, state.selectedSchoolCode, yearMonth)
        if (yearMonth != state.yearMonth) {
            val firstWeekday = yearMonth.getFirstWeekday()
            updateUiState(yearMonth = yearMonth, selectedDate = firstWeekday, entity = entity)
        } else {
            updateUiState(entity = entity)
        }
    }

    private suspend fun collectPreferences() {
        preferencesRepository.userPreferencesFlow.collectLatest {
            updateUiState(
                isLoading = (it.runningWorksCount != 0),
                screenMode = it.screenMode,
                selectedSchool = School(
                    name = it.schoolName,
                    schoolCode = it.schoolCode,
                )
            )
        }
    }

    fun getContentDescription(date: Date): String {
        val dailyState = state.monthlyDataState.find { it.date == date }

        val isSelectedString = if (date == state.selectedDate) "선택됨" else ""
        val isTodayString = if (date == DateUtil.today()) "오늘" else ""
        val dailyStateString = if (dailyState != null) {
            "식단: ${dailyState.mealUiState.description}\n학사일정:${dailyState.scheduleUiState.description}"
        } else {
            ""
        }

        return listOf(isSelectedString, isTodayString, dailyStateString)
            .filter { it.isNotEmpty() }
            .joinToString(", ")
    }

    fun getClickLabel(date: Date): String =
        if (date == state.selectedDate) "" else "식단 및 학사일정 보기"

    fun openNutrientPopup() {
        updateUiState(isNutrientPopupVisible = true)
    }

    fun closeNutrientPopup() {
        updateUiState(isNutrientPopupVisible = false)
    }

    fun openMemoPopup() {
        updateUiState(isMemoPopupVisible = true)
    }

    fun closeMemoPopup() {
        updateUiState(isMemoPopupVisible = false)
    }

    fun getCustomActions(date: Date): ImmutableList<CustomAccessibilityAction> {
        return state.monthlyDataState.getCustomActions(date)
    }

    private fun List<DailyData>.getCustomActions(date: Date): ImmutableList<CustomAccessibilityAction> {
        val (_, month, day) = date
        return this.find { it.date == date }?.let {
            val actions = mutableListOf<CustomAccessibilityAction>()
            if (!it.mealUiState.isEmpty) {
                actions.add(createNutrientPopupCustomAction(month, day))
            }
            actions.add(createMemoPopupCustomAction(month, day))
            actions.toImmutableList()
        } ?: persistentListOf()
    }

    private fun createNutrientPopupCustomAction(month: Int, day: Int): CustomAccessibilityAction {
        return CustomAccessibilityAction("${month}월 ${day}일 영양 보기") {
            openNutrientPopup()
            true
        }
    }

    private fun createMemoPopupCustomAction(month: Int, day: Int): CustomAccessibilityAction {
        return CustomAccessibilityAction("${month}월 ${day}일 메모 보기") {
            openMemoPopup()
            true
        }
    }

}

private data class CacheKey(val schoolCode: Int, val yearMonth: YearMonth)

private val MainUiState.cacheKey: CacheKey
    get() = CacheKey(selectedSchoolCode, yearMonth)

private fun MonthlyData.getMeal(date: Date): MealUiState {
    return try {
        meals.first { it.dateEquals(date) }
            .toMealUiState()
    } catch (e: NoSuchElementException) {
        MealUiState.EmptyMealState
    }
}

private fun MonthlyData.getSchedule(date: Date): ScheduleUiState {
    return try {
        val uiSchedules = schedules.filter { it.dateEquals(date) }.map { it.toUiSchedule() }
        ScheduleUiState(uiSchedules.toPersistentList())
    } catch (e: NoSuchElementException) {
        ScheduleUiState.EmptyScheduleState
    }
}

private fun MonthlyData.getMemo(date: Date): MemoUiState {
    return try {
        val memos = memos.filter { it.dateEquals(date) }.map { it.toUiMemo() }
        MemoUiState(
            year = date.year,
            month = date.month,
            day = date.dayOfMonth,
            memos = memos.toPersistentList()
        )
    } catch (e: NoSuchElementException) {
        MemoUiState.EmptyMemoUiState
    }
}

private fun Meal.dateEquals(date: Date) =
    this.year == date.year && this.month == date.month && this.day == date.dayOfMonth

private fun Schedule.dateEquals(date: Date) =
    this.year == date.year && this.month == date.month && this.day == date.dayOfMonth

private fun Memo.dateEquals(date: Date) =
    this.year == date.year && this.month == date.month && this.day == date.dayOfMonth