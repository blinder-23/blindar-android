package com.practice.hanbitlunch.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.combine.LoadMealScheduleDataUseCase
import com.example.domain.combine.MealScheduleEntity
import com.practice.database.meal.entity.MealEntity
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.hanbitlunch.calendar.YearMonth
import com.practice.preferences.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
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

    private val _scheduleDates: MutableStateFlow<Set<LocalDate>>
    val scheduleDates: StateFlow<Set<LocalDate>>
        get() = _scheduleDates

    private val selectedDateFlow: MutableStateFlow<LocalDate>

    private var cache: MutableMap<YearMonth, MealScheduleEntity>
    private var job: Job?

    init {
        val current = LocalDate.now()
        _uiState = mutableStateOf(
            MainUiState(
                year = current.year,
                month = current.monthValue,
                selectedDate = current,
                mealUiState = MealUiState.EmptyMealState,
                scheduleUiState = ScheduleUiState.EmptyScheduleState,
            )
        )
        selectedDateFlow = MutableStateFlow(current)
        _scheduleDates = MutableStateFlow(emptySet())
        cache = mutableMapOf()
        job = null
    }

    /**
     * init 블럭에서 실행하지 않은 이유는 [IllegalStateException]이 발생하기 때문이다.
     * 아직 UI에 반영되지 않은 값을 참조하기 때문에 예외가 발생한다.
     */
    fun onLaunch() {
        viewModelScope.launch(Dispatchers.IO) {
            loadMonthlyData(state.selectedDate)
        }
        viewModelScope.launch(Dispatchers.IO) {
            collectPreferences()
        }
    }

    /**
     * Kotlin Flow의 combine 함수를 본따 작성했다.
     */
    private fun updateUiState(
        selectedDate: LocalDate = state.selectedDate,
        entity: MealScheduleEntity? = cache[selectedDate.yearMonth],
        isLoading: Boolean = false,
    ) {
        val newMealUiState = entity?.getMeal(selectedDate) ?: state.mealUiState
        val newScheduleUiState = entity?.getSchedule(selectedDate) ?: state.scheduleUiState
        synchronized(state) {
            state = state.copy(
                selectedDate = selectedDate,
                mealUiState = newMealUiState,
                scheduleUiState = newScheduleUiState,
                isLoading = isLoading,
            )
        }
        entity?.let { updateScheduleDates(it) }
    }

    private fun updateScheduleDates(entity: MealScheduleEntity) {
        _scheduleDates.value = scheduleDates.value.toMutableSet().apply {
            addAll(entity.schedules.map { LocalDate.of(it.year, it.month, it.day) })
        }
    }

    fun onDateClick(clickedDate: LocalDate) = viewModelScope.launch(Dispatchers.IO) {
        loadMonthlyData(clickedDate)
        updateUiState(selectedDate = clickedDate)
    }

    private suspend fun loadMonthlyData(date: LocalDate) {
        if (cache.containsKey(date.yearMonth)) {
            return
        }
        val (queryYear, queryMonth) = date.yearMonth
        job?.cancelAndJoin()
        job = viewModelScope.launch(Dispatchers.IO) {
            loadMealScheduleDataUseCase.loadData(queryYear, queryMonth).collectLatest {
                cache[date.yearMonth] = it
                updateUiState(entity = it)
            }
        }
    }

    fun onSwiped(yearMonth: YearMonth) {
        val (year, month) = yearMonth
        state = state.copy(
            year = year,
            month = month,
        )
    }

    private suspend fun collectPreferences() {
        preferencesRepository.userPreferencesFlow.collectLatest {
            updateUiState(isLoading = (it.runningWorksCount != 0))
        }
    }

    fun getContentDescription(date: LocalDate): String {
        return if (date == state.selectedDate) {
            val mealState = state.mealUiState
            val scheduleUiState = state.scheduleUiState
            "식단: ${mealState.description}\n학사일정:${scheduleUiState.description}"
        } else {
            ""
        }
    }

    fun getClickLabel(date: LocalDate): String =
        if (date == state.selectedDate) "" else "식단 및 학사일정 보기"

}

private val LocalDate.yearMonth: YearMonth
    get() = YearMonth(year, monthValue)

private fun MealScheduleEntity.getMeal(date: LocalDate): MealUiState {
    return try {
        meals.first { it.dateEquals(date) }
            .toMealUiState()
    } catch (e: NoSuchElementException) {
        MealUiState.EmptyMealState
    }
}

private fun MealScheduleEntity.getSchedule(date: LocalDate): ScheduleUiState {
    return try {
        val schedules = schedules.filter { it.dateEquals(date) }.map { it.toSchedule() }
        ScheduleUiState(schedules.toPersistentList())
    } catch (e: NoSuchElementException) {
        ScheduleUiState.EmptyScheduleState
    }
}

private fun MealEntity.dateEquals(date: LocalDate) =
    this.year == date.year && this.month == date.monthValue && this.day == date.dayOfMonth

private fun ScheduleEntity.dateEquals(date: LocalDate) =
    this.year == date.year && this.month == date.monthValue && this.day == date.dayOfMonth