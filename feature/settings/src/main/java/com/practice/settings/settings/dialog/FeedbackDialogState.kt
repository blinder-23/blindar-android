package com.practice.settings.settings.dialog

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.practice.util.getAppVersionName
import kotlin.math.min

@Stable
class FeedbackDialogState internal constructor(
    feedbackText: String = "",
    isError: Boolean = false
) {
    var feedbackText by mutableStateOf(feedbackText)
        private set

    var isError: Boolean by mutableStateOf(isError)
        private set

    fun onFeedbackTextUpdate(value: String) {
        feedbackText = value.substring(0..min(1000, value.lastIndex))
        isError = false
    }

    fun onError() {
        isError = true
    }

    fun clear() {
        feedbackText = ""
        isError = false
    }

    fun sendOnlyWhenTextIsNotEmpty(context: Context, onSend: (String, String) -> Unit) {
        if (feedbackText.isEmpty()) {
            onError()
        } else {
            val versionName = context.getAppVersionName()
            onSend(versionName, feedbackText.trim())
        }
    }

    companion object {
        internal val Saver = listSaver(
            save = { listOf(it.feedbackText, it.isError) },
            restore = { FeedbackDialogState(it[0] as String, it[1] as Boolean) }
        )
    }
}

@Composable
fun rememberFeedbackDialogState(
    feedbackText: String = "",
    isError: Boolean = false,
) = rememberSaveable(saver = FeedbackDialogState.Saver) {
    FeedbackDialogState(feedbackText, isError)
}