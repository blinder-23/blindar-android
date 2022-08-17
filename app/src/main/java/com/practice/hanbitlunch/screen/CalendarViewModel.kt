package com.practice.hanbitlunch.screen

import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CalendarViewModel {

}

data class CalendarUiState(
    val year: Int
)