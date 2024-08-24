package com.practice.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.api.school.RemoteSchoolRepository
import com.practice.api.toSchool
import com.practice.domain.School
import com.practice.preferences.PreferencesRepository
import com.practice.util.removeWhitespaces
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val schoolRepository: RemoteSchoolRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val _registerUiState = MutableStateFlow(RegisterUiState.Empty)
    val registerUiState: StateFlow<RegisterUiState>
        get() = _registerUiState.asStateFlow()

    private var schoolListJob: Job? = null

    init {
        viewModelScope.launch {
            collectSelectedSchool()
        }
        updateSchoolList("")
    }

    /**
     * SelectSchoolScreen
     */
    private suspend fun collectSelectedSchool() {
        preferencesRepository.userPreferencesFlow.collectLatest { preferences ->
            _registerUiState.update {
                it.copy(
                    selectedSchool = School(
                        name = preferences.schoolName,
                        schoolCode = preferences.schoolCode,
                    )
                )
            }
        }
    }

    fun onSchoolQueryChange(query: String) {
        val queryWithoutWhitespaces = query.removeWhitespaces()
        _registerUiState.update {
            it.copy(schoolQuery = queryWithoutWhitespaces)
        }
        updateSchoolList(queryWithoutWhitespaces)
    }

    private fun updateSchoolList(query: String) {
        schoolListJob?.cancel()
        schoolListJob = viewModelScope.launch {
            val schools = schoolRepository.searchSupportedSchools(query)
                .map { it.toSchool() }
                .toImmutableList()
            _registerUiState.update {
                it.copy(schools = schools)
            }
        }
    }

    fun onSchoolClick(
        context: Context,
        school: School,
        onSchoolClickCallbackFromUI: () -> Unit,
    ) {
        viewModelScope.launch {
            preferencesRepository.updateSelectedSchool(school.schoolCode, school.name)
            BlindarWorkManager.setOneTimeFetchDataWork(context)
            BlindarWorkManager.setUserInfoToRemoteWork(context)
            onSchoolClickCallbackFromUI()
        }
    }
}