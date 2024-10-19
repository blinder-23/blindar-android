package com.practice.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.api.user.RemoteUserRepository
import com.practice.util.removeWhitespaces
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterFormViewModel @Inject constructor(
    private val remoteUserRepository: RemoteUserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterFormUiState.Empty)
    val uiState: StateFlow<RegisterFormUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name.removeWhitespaces(),
                isDuplicateName = false,
            )
        }
    }

    fun checkUsernameIsDuplicated(
        onNotDuplicated: (String) -> Unit, // username
        onDuplicated: () -> Unit,
    ) {
        val updateUiStateOnDuplicated = {
            onDuplicated()
            _uiState.update {
                it.copy(isDuplicateName = true)
            }
        }

        viewModelScope.launch {
            val username = uiState.value.name
            if (uiState.value.isNameValid && !remoteUserRepository.isUsernameDuplicate(username)) {
                onNotDuplicated(username)
            } else {
                updateUiStateOnDuplicated()
            }
        }
    }
}