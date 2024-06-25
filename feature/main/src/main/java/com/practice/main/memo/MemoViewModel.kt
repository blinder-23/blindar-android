package com.practice.main.memo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.practice.api.memo.RemoteMemoRepository
import com.practice.main.state.UiMemo
import com.practice.main.state.toMemo
import com.practice.main.state.toUiMemo
import com.practice.main.state.toUiSchedule
import com.practice.memo.MemoRepository
import com.practice.schedule.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val scheduleRepository: ScheduleRepository,
    private val localMemoRepository: MemoRepository,
    private val remoteMemoRepository: RemoteMemoRepository,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<MemoRoute>()

    private val _uiState = MutableStateFlow<MemoUiState>(
        MemoUiState.Loading(
            route.date,
            route.userId,
            route.schoolCode,
        )
    )
    val uiState: StateFlow<MemoUiState> = _uiState.asStateFlow()

    private val _bottomSheetState = MutableStateFlow<UiMemo?>(null)
    val bottomSheetState: StateFlow<UiMemo?> = _bottomSheetState.asStateFlow()

    init {
        viewModelScope.launch {
            loadSchedulesAndMemos()
        }
    }

    private suspend fun loadSchedulesAndMemos() {
        _uiState.value = loadingUiState()
        _uiState.value = successOrFailState()
    }

    private fun loadingUiState() = MemoUiState.Loading(route.date, route.userId, route.schoolCode)

    private suspend fun successOrFailState(): MemoUiState {
        return try {
            tryLoadSchedulesAndMemos()
        } catch (e: Exception) {
            e.printStackTrace()
            MemoUiState.Fail(
                route.date,
                route.userId,
                route.schoolCode,
            )
        }
    }

    private suspend fun tryLoadSchedulesAndMemos(): MemoUiState.Success {
        val schedules = scheduleRepository.getSchedules(route.schoolCode, route.date)
        val memos = localMemoRepository.getMemos(route.userId, route.date)
        return MemoUiState.Success(
            route.date,
            route.userId,
            route.schoolCode,
            schedules.toUiSchedule(),
            memos.toUiMemo(),
        )
    }

    fun onMemoEdit(uiMemo: UiMemo) {
        _bottomSheetState.value = uiMemo
    }

    fun onMemoDelete(uiMemo: UiMemo) {
        viewModelScope.launch {
            localMemoRepository.deleteMemo(uiMemo.id)
            remoteMemoRepository.deleteMemo(uiMemo.id)
            loadSchedulesAndMemos()
        }
    }

    fun onMemoEditCancel() {
        clearBottomSheetState()
    }

    fun onMemoEditSubmit(uiMemo: UiMemo) {
        viewModelScope.launch {
            localMemoRepository.updateMemo(uiMemo.toMemo())
            remoteMemoRepository.updateMemo(uiMemo.toMemo())
            clearBottomSheetState()
            loadSchedulesAndMemos()
        }
    }

    private fun clearBottomSheetState() {
        _bottomSheetState.value = null
    }
}