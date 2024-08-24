package com.practice.username

import androidx.lifecycle.ViewModel
import com.practice.firebase.BlindarFirebase
import com.practice.util.removeWhitespaces
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterFormViewModel @Inject constructor() : ViewModel() {

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

    fun submitName(
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        val onUsernameDuplicate = {
            onFail()
            _uiState.update {
                it.copy(isDuplicateName = true)
            }
        }

        // TODO: Firebase 확인 로직 제거하고, 서버 API로 migrate
        if (uiState.value.isNameValid) {
            BlindarFirebase.tryStoreUsername(
                username = uiState.value.name,
                onSuccess = onSuccess,
                onFail = onUsernameDuplicate,
            )
        } else {
            onUsernameDuplicate()
        }
    }
}