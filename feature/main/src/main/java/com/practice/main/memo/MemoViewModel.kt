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

    private val _bottomSheetState = MutableStateFlow<MemoBottomSheetState?>(null)
    val bottomSheetState: StateFlow<MemoBottomSheetState?> = _bottomSheetState.asStateFlow()

    private val _deletionTargetMemo = MutableStateFlow<UiMemo?>(null)
    val deletionTargetMemo: StateFlow<UiMemo?> = _deletionTargetMemo.asStateFlow()

    init {
        viewModelScope.launch {
            refreshSchedulesAndMemos()
        }
    }

    private suspend fun refreshSchedulesAndMemos() {
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

    fun onAddMemoButtonClick() {
        _bottomSheetState.value =
            MemoBottomSheetState.Add(UiMemo.getEmptyMemo(userId = route.userId))
    }

    fun onEditMemoButtonClick(uiMemo: UiMemo) {
        _bottomSheetState.value = MemoBottomSheetState.Update(uiMemo)
    }

    fun onMemoBottomSheetUpdate(state: MemoBottomSheetState) {
        _bottomSheetState.value = state
    }

    fun onMemoDeleteIconClick(uiMemo: UiMemo) {
        _deletionTargetMemo.value = uiMemo
    }

    fun onMemoEditDismiss() {
        clearBottomSheetState()
    }

    fun onMemoEditSubmit(state: MemoBottomSheetState) {
        viewModelScope.launch {
            when (state) {
                is MemoBottomSheetState.Add -> onMemoAddSubmit(state)
                is MemoBottomSheetState.Update -> onMemoUpdateSubmit(state)
            }
            refreshSchedulesAndMemos()
            clearBottomSheetState()
        }
    }

    private suspend fun onMemoAddSubmit(state: MemoBottomSheetState.Add) {
        val memo = state.uiMemo.toMemo()
        val assignedMemoId = remoteMemoRepository.updateMemo(memo)
        localMemoRepository.insertMemo(memo.copy(id = assignedMemoId, isSavedOnRemote = true))
    }

    private suspend fun onMemoUpdateSubmit(state: MemoBottomSheetState.Update) {
        val memo = state.uiMemo.toMemo()
        remoteMemoRepository.updateMemo(memo)
        localMemoRepository.updateMemo(memo)
    }

    private fun clearBottomSheetState() {
        _bottomSheetState.value = null
    }

    fun clearDeletionTargetMemo() {
        _deletionTargetMemo.value = null
    }

    fun deleteTargetMemo(target: UiMemo) {
        viewModelScope.launch {
            localMemoRepository.deleteMemo(target.id)
            remoteMemoRepository.deleteMemo(target.id)
            refreshSchedulesAndMemos()
            clearDeletionTargetMemo()
        }
    }
}