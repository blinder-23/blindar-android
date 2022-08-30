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
    private val cache = mutableMapOf<YearMonth, MealScheduleEntity>()

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
        viewModelScope.launch {
            onDateClick(current)
        }
    }

    suspend fun onDateClick(clickedDate: LocalDate) {
        loadMonthlyData(clickedDate.year, clickedDate.monthValue)

        val monthlyData = cache[clickedDate.yearMonth]!!
        _uiState.value = uiState.value.copy(
            mealUiState = monthlyData.getMeal(clickedDate),
            scheduleUiState = monthlyData.getSchedule(clickedDate)
        )
    }

    private suspend fun loadMonthlyData(year: Int, month: Int) {
        if (!cache.containsKey(YearMonth(year, month))) {
            val entity = loadMealScheduleDataUseCase.loadData(year, month)
            cache[YearMonth(year, month)] = entity
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