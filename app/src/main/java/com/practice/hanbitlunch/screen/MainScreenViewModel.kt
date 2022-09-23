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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val loadMealScheduleDataUseCase: LoadMealScheduleDataUseCase,
) : ViewModel() {

    private val _uiState: MutableState<MainUiState>
    val uiState: State<MainUiState>
        get() = _uiState
    private var cache: MealScheduleEntity?

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
        cache = null
        viewModelScope.launch(Dispatchers.IO) {
            onDateClick(current)
        }
    }

    suspend fun onDateClick(clickedDate: LocalDate) {
        val (clickedYear, clickedMonth) = clickedDate.yearMonth
        if (cache == null || (cache!!.year != clickedYear) || (cache!!.month != clickedMonth)) {
            // load..
            loadMonthlyData(clickedYear, clickedMonth).join()
        }
        _uiState.value = uiState.value.copy(
            mealUiState = cache!!.getMeal(clickedDate),
            scheduleUiState = cache!!.getSchedule(clickedDate)
        )
    }

    private fun loadMonthlyData(year: Int, month: Int) = viewModelScope.launch {
        cache = loadMealScheduleDataUseCase.loadData(year, month).first()
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