package com.practice.settings.feedback

import android.os.Build
import androidx.lifecycle.ViewModel
import com.practice.api.feedback.RemoteFeedbackRepository
import com.practice.api.feedback.repository.FeedbackResult
import com.practice.firebase.BlindarFirebase
import com.practice.firebase.BlindarUserStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackRepository: RemoteFeedbackRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeedbackScreenState())
    val uiState: StateFlow<FeedbackScreenState> = _uiState.asStateFlow()

    fun updateFeedback(feedback: String) {
        _uiState.update {
            it.copy(
                feedback = feedback,
                isError = false,
            )
        }
    }

    fun canSendFeedback(feedback: String): Boolean {
        return feedback.isNotEmpty().apply {
            _uiState.update {
                it.copy(isError = !this)
            }
        }
    }

    suspend fun sendFeedback(request: FeedbackUiRequest): Boolean {
        val (feedback, appVersionName) = request
        if (!canSendFeedback(request.feedback)) {
            return false
        }

        val userId = getUserId() ?: return false
        val deviceName = Build.MODEL ?: "UNKNOWN_DEVICE"
        val osVersion = Build.VERSION.SDK_INT.toString()

        return feedbackRepository.sendFeedback(
            userId = userId,
            deviceName = deviceName,
            osVersion = osVersion,
            appVersion = appVersionName,
            contents = feedback
        ) is FeedbackResult.Success
    }

    private fun getUserId(): String? {
        return when (val user = BlindarFirebase.getBlindarUser()) {
            is BlindarUserStatus.LoginUser -> user.user.uid
            else -> null
        }
    }
}