package com.practice.hanbitlunch.screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.combine.LoadMealScheduleDataUseCase
import com.example.domain.combine.MealScheduleEntity
import com.example.server.meal.RemoteMealRepository
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
    private val remoteMealRepository: RemoteMealRepository,
) : ViewModel() {

    private val _uiState: MutableState<MainUiState>
    val uiState: State<MainUiState>
        get() = _uiState

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
        cache = mutableMapOf()
        job = null
        onTest()
    }

    private fun onTest() = viewModelScope.launch(Dispatchers.IO) {
        (1..12).forEach { month ->
            val fromRemote = remoteMealRepository.getMeals(2022, month)
            Log.d("MainScreenViewModel", "meal $month from remote: ${fromRemote.response.size}")
        }
    }

    /**
     * init 블럭에서 실행하지 않은 이유는 [IllegalStateException]이 발생하기 때문이다.
     * 아직 UI에 반영되지 않은 값을 참조하기 때문에 예외가 발생한다.
     */
    fun onLaunch() = viewModelScope.launch(Dispatchers.IO) {
        loadMonthlyData(uiState.value.selectedDate)
    }

    /**
     * Kotlin Flow의 combine 함수를 본따 작성했다.
     */
    private fun updateUiState(
        selectedDate: LocalDate = uiState.value.selectedDate,
        entity: MealScheduleEntity? = cache[selectedDate.yearMonth]
    ) {
        val newMealUiState = entity?.getMeal(selectedDate) ?: uiState.value.mealUiState
        val newScheduleUiState = entity?.getSchedule(selectedDate) ?: uiState.value.scheduleUiState
        _uiState.value = uiState.value.copy(
            selectedDate = selectedDate,
            mealUiState = newMealUiState,
            scheduleUiState = newScheduleUiState
        )
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
                Log.d("MainScreenViewModel", "meal for ${date.month}: ${it.meals.size}")
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