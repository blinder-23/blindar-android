package com.practice.main.main.state

import com.practice.domain.School

sealed interface PreferencesState {
    val isRefreshing: Boolean
    val selectedSchool: School
    val mainUiMode: MainUiMode

    data class Loading(
        override val isRefreshing: Boolean = false,
        override val selectedSchool: School = School.EmptySchool,
        override val mainUiMode: MainUiMode = MainUiMode.Loading,
    ) : PreferencesState

    data class Loaded(
        override val isRefreshing: Boolean,
        override val selectedSchool: School,
        override val mainUiMode: MainUiMode,
    ) : PreferencesState
}
