package com.practice.main.memo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hsk.ktx.date.Date
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.BlindarButton
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.components.BlindarTopAppBarDefaults
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.HeadlineMedium
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.main.previewMemos
import com.practice.main.main.previewSchedules
import com.practice.main.state.UiMemo
import kotlinx.coroutines.launch

@Composable
fun MemoScreen(
    onNavigateToBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MemoViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    MemoScreen(
        uiState = state,
        onNavigateToBack = onNavigateToBack,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoScreen(
    uiState: MemoUiState,
    onNavigateToBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            BlindarTopAppBar(
                title = stringResource(id = R.string.memo_header),
                navigationIcon = {
                    BlindarTopAppBarDefaults.NavigationIcon(onNavigateToBack)
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            )
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .widthIn(500.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MemoScreenHeader(
                uiState = uiState,
                onAddMemoButtonClick = {
                    coroutineScope.launch {
                        modalBottomSheetState.expand()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            when (uiState) {
                is MemoUiState.Loading -> MemoScreenLoading(modifier = Modifier.fillMaxSize())
                is MemoUiState.Success -> MemoScreenSuccess(
                    uiState = uiState,
                    onMemoEdit = {},
                    onMemoDelete = {},
                    modifier = Modifier.fillMaxSize(),
                )

                is MemoUiState.Fail -> MemoScreenFail(modifier = Modifier.fillMaxSize())
            }
        }

        if (modalBottomSheetState.isVisible) {
            ModalBottomSheet(onDismissRequest = { coroutineScope.launch { modalBottomSheetState.hide() } }) {
                BodyLarge(text = "Hello world!".repeat(50))
            }
        }
    }

    // TODO: 삭제 dialog: 별도의 StateFlow 선언하기
}

@Composable
private fun MemoScreenHeader(
    uiState: MemoUiState,
    onAddMemoButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val date = uiState.date
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        HeadlineMedium(
            text = stringResource(
                id = R.string.memo_date_format,
                date.year, date.month, date.dayOfMonth, date.dayOfWeek.shortName,
            ),
        )
        Spacer(modifier = Modifier.height(24.dp))
        BlindarButton(
            onClick = onAddMemoButtonClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            BodyLarge(
                text = stringResource(id = R.string.memo_add_button),
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun MemoScreenLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun MemoScreenSuccess(
    uiState: MemoUiState.Success,
    onMemoEdit: (UiMemo) -> Unit,
    onMemoDelete: (UiMemo) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(uiState.uiSchedules) {
            ScheduleItem(
                item = it,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        items(uiState.uiMemos) {
            MemoItem(
                item = it,
                onEditButtonClick = onMemoEdit,
                onDeleteButtonClick = onMemoDelete,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun MemoScreenFail(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TitleMedium(
            text = stringResource(id = R.string.memo_fail),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.weight(10f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditMemoBottomSheet(
    memo: UiMemo,
    onDismissRequest: () -> Unit,
    onSubmitUpdate: (UiMemo) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        TitleLarge(text = "dtd")
    }
}

@DarkPreview
@Composable
private fun MemoScreenPreview_Success() {
    BlindarTheme {
        MemoScreen(
            uiState = MemoUiState.Success(
                date = Date.now(),
                userId = "",
                schoolCode = 123,
                uiSchedules = previewSchedules,
                uiMemos = previewMemos.plus(previewMemos),
            ),
            onNavigateToBack = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@DarkPreview
@Composable
private fun MemoScreenPreview_Loading() {
    BlindarTheme {
        MemoScreen(
            uiState = MemoUiState.Loading(
                date = Date.now(),
                userId = "",
                schoolCode = 123,
            ),
            onNavigateToBack = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@DarkPreview
@Composable
private fun MemoScreenPreview_Fail() {
    BlindarTheme {
        MemoScreen(
            uiState = MemoUiState.Fail(
                date = Date.now(),
                userId = "",
                schoolCode = 123,
            ),
            onNavigateToBack = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}