package com.practice.main

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsk.ktx.date.Date
import com.practice.combine.LoadMealScheduleDataUseCase
import com.practice.combine.MealScheduleEntity
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.designsystem.calendar.core.getFirstWeekday
import com.practice.designsystem.calendar.core.yearMonth
import com.practice.main.state.DailyMealScheduleState
import com.practice.main.state.MainUiState
import com.practice.main.state.MealUiState
import com.practice.main.state.ScheduleUiState
import com.practice.main.state.toMealUiState
import com.practice.main.state.toSchedule
import com.practice.meal.entity.MealEntity
import com.practice.preferences.PreferencesRepository
import com.practice.preferences.ScreenMode
import com.practice.schedule.entity.ScheduleEntity
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val loadMealScheduleDataUseCase: LoadMealScheduleDataUseCase,
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
    private val cache: MutableMap<YearMonth, MealScheduleEntity>

    init {
        val current = Date.now()
        _uiState = mutableStateOf(
            MainUiState(
                year = current.year,
                month = current.month,
                selectedDate = current,
                monthlyMealScheduleState = emptyList(),
                isLoading = false,
                screenMode = ScreenMode.Default,
            )
        )
        selectedDateFlow = MutableStateFlow(current)
        _scheduleDates = MutableStateFlow(emptySet())
        cache = mutableMapOf()
    }

    /**
     * init 블럭에서 실행하지 않은 이유는 [IllegalStateException]이 발생하기 때문이다.
     * 아직 UI에 반영되지 않은 값을 참조하기 때문에 예외가 발생한다.
     */
    fun onLaunch() {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = loadMonthlyData(state.yearMonth)
            updateUiState(entity = entity)
        }
        viewModelScope.launch(Dispatchers.IO) {
            collectPreferences()
        }
    }

    fun onRefresh(context: Context) {
        enqueueOneTimeWork(context)
    }

    private fun enqueueOneTimeWork(context: Context) {
        BlindarWorkManager.setOneTimeWork(context = context)
    }

    /**
     * Kotlin Flow의 combine 함수를 본따 작성했다.
     */
    private fun updateUiState(
        yearMonth: YearMonth = state.yearMonth,
        selectedDate: Date = state.selectedDate,
        entity: MealScheduleEntity? = cache[selectedDate.yearMonth],
        isLoading: Boolean = state.isLoading,
        screenMode: ScreenMode = state.screenMode,
    ) {
        val monthlyMealScheduleState = if (entity != null) {
            parseDailyState(entity)
        } else {
            emptyList()
        }
        synchronized(state) {
            state = state.copy(
                year = yearMonth.year,
                month = yearMonth.month,
                monthlyMealScheduleState = monthlyMealScheduleState,
                selectedDate = selectedDate,
                isLoading = isLoading,
                screenMode = screenMode,
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

    private fun updateScheduleDates(entity: MealScheduleEntity) {
        _scheduleDates.value = scheduleDates.value.toMutableSet().apply {
            addAll(entity.schedules.map { Date(it.year, it.month, it.day) })
        }
    }

    fun onDateClick(clickedDate: Date) = viewModelScope.launch(Dispatchers.IO) {
        val entity = loadMonthlyData(clickedDate.yearMonth)
        updateUiState(
            yearMonth = clickedDate.yearMonth,
            selectedDate = clickedDate,
            entity = entity
        )
    }

    private suspend fun loadMonthlyData(yearMonth: YearMonth): MealScheduleEntity {
        val (queryYear, queryMonth) = yearMonth
        return if (cache.containsKey(yearMonth)) {
            cache[yearMonth]!!
        } else {
            loadMealScheduleDataUseCase.loadData(queryYear, queryMonth).first().apply {
                cache[yearMonth] = this
            }
        }
    }

    private fun parseDailyState(mealScheduleEntity: MealScheduleEntity): List<DailyMealScheduleState> {
        val allDates = mutableSetOf<Date>().apply {
            addAll(mealScheduleEntity.meals.map { Date(it.year, it.month, it.day) })
            addAll(mealScheduleEntity.schedules.map { Date(it.year, it.month, it.day) })
        }
        val newDailyData = allDates.map { date ->
            val meal = mealScheduleEntity.getMeal(date)
            val schedule = mealScheduleEntity.getSchedule(date)
            DailyMealScheduleState(
                date = date,
                mealUiState = meal,
                scheduleUiState = schedule,
            )
        }.sorted()
        return newDailyData
    }

    fun onSwiped(yearMonth: YearMonth) = viewModelScope.launch {
        val entity = loadMonthlyData(yearMonth)
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
            )
        }
    }

    fun getContentDescription(date: Date): String {
        return if (date == state.selectedDate) {
            val dailyState = state.monthlyMealScheduleState.find { it.date == date }
            if (dailyState != null) {
                "식단: ${dailyState.mealUiState.description}\n학사일정:${dailyState.scheduleUiState.description}"
            } else {
                ""
            }
        } else {
            ""
        }
    }

    fun getClickLabel(date: Date): String =
        if (date == state.selectedDate) "" else "식단 및 학사일정 보기"

}

private fun MealScheduleEntity.getMeal(date: Date): MealUiState {
    return try {
        meals.first { it.dateEquals(date) }
            .toMealUiState()
    } catch (e: NoSuchElementException) {
        MealUiState.EmptyMealState
    }
}

private fun MealScheduleEntity.getSchedule(date: Date): ScheduleUiState {
    return try {
        val schedules = schedules.filter { it.dateEquals(date) }.map { it.toSchedule() }
        ScheduleUiState(schedules.toPersistentList())
    } catch (e: NoSuchElementException) {
        ScheduleUiState.EmptyScheduleState
    }
}

private fun MealEntity.dateEquals(date: Date) =
    this.year == date.year && this.month == date.month && this.day == date.dayOfMonth

private fun ScheduleEntity.dateEquals(date: Date) =
    this.year == date.year && this.month == date.month && this.day == date.dayOfMonth