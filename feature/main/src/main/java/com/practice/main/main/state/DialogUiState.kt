package com.practice.main.main.state

data class DialogUiState(
    val isMealDialogVisible: Boolean,
    val isScheduleDialogVisible: Boolean,
) {
    companion object {
        val EMPTY = DialogUiState(
            isMealDialogVisible = false,
            isScheduleDialogVisible = false,
        )
    }
}