package com.practice.selectschool

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.practice.api.school.RemoteSchoolRepository
import com.practice.api.toSchool
import com.practice.api.user.RemoteUserRepository
import com.practice.domain.School
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import com.practice.preferences.PreferencesRepository
import com.practice.util.removeWhitespaces
import com.practice.work.BlindarWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectSchoolViewModel @Inject constructor(
    private val schoolRepository: RemoteSchoolRepository,
    private val preferencesRepository: PreferencesRepository,
    private val remoteUserRepository: RemoteUserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SelectSchoolUiState.Empty.copy(username = savedStateHandle.get<String>("username")))
    val uiState: StateFlow<SelectSchoolUiState>
        get() = _uiState.asStateFlow()

    private var schoolListJob: Job? = null

    init {
        viewModelScope.launch {
            collectSelectedSchool()
        }
        updateSchoolList("")
    }

    private suspend fun collectSelectedSchool() {
        preferencesRepository.userPreferencesFlow.collectLatest { preferences ->
            _uiState.update {
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
        _uiState.update {
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
            _uiState.update {
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
            try {
                preferencesRepository.updateSelectedSchool(school.schoolCode, school.name)
                remoteUserRepository.updateUserInfo(getUserID(), school.schoolCode, getUsername())

                uiState.value.username?.let { username ->
                    BlindarFirebase.tryStoreUsername(username = username)
                }

                BlindarWorkManager.setOneTimeFetchDataWork(context)
                BlindarWorkManager.setUserInfoToRemoteWork(context)

                withContext(Dispatchers.Main) {
                    onSchoolClickCallbackFromUI()
                }
            } catch (e: IllegalStateException) {
                return@launch
            }
        }
    }

    private fun getUserID(): String {
        return if (uiState.value.username == null) {
            ""
        } else {
            getLoginUserOrNull()?.uid ?: ""
        }
    }

    private fun getUsername(): String {
        return uiState.value.username ?: (getLoginUserOrNull()?.displayName
            ?: throw IllegalStateException("SelectSchoolUiState.username and firebase username are both null"))
    }

    private fun getLoginUserOrNull(): FirebaseUser? {
        val blindarUser = BlindarFirebase.getBlindarUser()
        return if (blindarUser is BlindarUserStatus.LoginUser) {
            blindarUser.user
        } else {
            null
        }
    }
}