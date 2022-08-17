package com.practice.hanbitlunch.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.domain.combine.LoadMealScheduleDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val loadMealScheduleDataUseCase: LoadMealScheduleDataUseCase,
) : ViewModel() {

    val uiState: MutableState<CalendarUiState>

    init {
        val current = LocalDate.now()
        uiState = mutableStateOf(
            CalendarUiState(
                year = current.year,
                month = current.monthValue,
                selectedDate = current,
            )
        )
    }


}

data class CalendarUiState(
    val year: Int,
    val month: Int,
    val selectedDate: LocalDate,
)

// TODO: how to show empty meal or schedule data?