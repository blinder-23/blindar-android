package com.practice.hanbitlunch.screen

import android.util.Log
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val loadMealScheduleDataUseCase: LoadMealScheduleDataUseCase,
) : ViewModel() {

    private val tag = "MainScreenViewModel"

    private val _uiState: MutableState<MainUiState>
    val uiState: State<MainUiState>
        get() = _uiState

    private val selectedDateFlow: MutableStateFlow<LocalDate>

    private var cache: MealScheduleEntity?
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
        cache = null
        job = null
    }

    fun onLaunch() = viewModelScope.launch(Dispatchers.IO) {
        loadMonthlyData(uiState.value.selectedDate)
    }

    suspend fun onDateClick(clickedDate: LocalDate) {
        loadMonthlyData(clickedDate)
        updateUiState(selectedDate = clickedDate)
    }

    private fun updateUiState(
        selectedDate: LocalDate = uiState.value.selectedDate,
        entity: MealScheduleEntity? = cache
    ) {
        val newMealUiState = entity?.getMeal(selectedDate) ?: uiState.value.mealUiState
        val newScheduleUiState = entity?.getSchedule(selectedDate) ?: uiState.value.scheduleUiState
        _uiState.value = uiState.value.copy(
            selectedDate = selectedDate,
            mealUiState = newMealUiState,
            scheduleUiState = newScheduleUiState
        )
        Log.d(tag, "$selectedDate, $newMealUiState, $newScheduleUiState")
    }

    private suspend fun loadMonthlyData(date: LocalDate) {
        val (queryYear, queryMonth) = date.yearMonth
        if (cache?.year == queryYear && cache?.month == queryMonth) {
            return
        }
        job?.cancelAndJoin()
        job = viewModelScope.launch(Dispatchers.IO) {
            loadMealScheduleDataUseCase.loadData(queryYear, queryMonth).collectLatest {
                Log.d(tag, "new value for $queryYear $queryMonth! $it")
                cache = it
                updateUiState(entity = it)
            }
        }
    }

    fun onSwiped(yearMonth: YearMonth) {
        val (year, month) = yearMonth
        _uiState.value = uiState.value.copy(
            year = year,
            month = month,
        )
    }

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
        val schedules = schedules.filter { it.dateEquals(date) }
            .map { Schedule(it.eventName) }
        ScheduleUiState(schedules.toPersistentList())
    } catch (e: NoSuchElementException) {
        ScheduleUiState.EmptyScheduleState
    }
}

private fun MealEntity.dateEquals(date: LocalDate) =
    this.year == date.year && this.month == date.monthValue && this.day == date.dayOfMonth

private fun ScheduleEntity.dateEquals(date: LocalDate) =
    this.year == date.year && this.month == date.monthValue && this.day == date.dayOfMonth