package com.practice.main.memo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
import com.practice.designsystem.components.LabelMedium
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.main.previewMemos
import com.practice.main.main.previewSchedules
import com.practice.main.state.UiMemo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoScreen(
    onNavigateToBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MemoViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val memoBottomSheetState by viewModel.bottomSheetState.collectAsStateWithLifecycle()

    MemoScreen(
        uiState = state,
        onNavigateToBack = onNavigateToBack,
        onAddMemoButtonClick = viewModel::onAddMemoButtonClick,
        onEditMemoButtonClick = viewModel::onEditMemoButtonClick,
        memoBottomSheetState = memoBottomSheetState,
        onMemoBottomSheetStateUpdate = viewModel::onMemoBottomSheetUpdate,
        onMemoBottomSheetDismiss = viewModel::onMemoEditDismiss,
        onMemoUpdate = viewModel::onMemoEditSubmit,
        onMemoDelete = viewModel::onMemoDelete,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoScreen(
    uiState: MemoUiState,
    onNavigateToBack: () -> Unit,
    onAddMemoButtonClick: () -> Unit,
    onEditMemoButtonClick: (UiMemo) -> Unit,
    memoBottomSheetState: MemoBottomSheetState?,
    onMemoBottomSheetStateUpdate: (MemoBottomSheetState) -> Unit,
    onMemoBottomSheetDismiss: () -> Unit,
    onMemoUpdate: (MemoBottomSheetState) -> Unit,
    onMemoDelete: (UiMemo) -> Unit,
    modifier: Modifier = Modifier,
    modalBottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
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
                        onAddMemoButtonClick()
                        modalBottomSheetState.expand()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            when (uiState) {
                is MemoUiState.Loading -> MemoScreenLoading(modifier = Modifier.fillMaxSize())
                is MemoUiState.Success -> MemoScreenSuccess(
                    uiState = uiState,
                    onMemoEdit = { uiMemo ->
                        coroutineScope.launch { modalBottomSheetState.expand() }
                        onEditMemoButtonClick(uiMemo)
                    },
                    onMemoDelete = onMemoDelete,
                    modifier = Modifier.fillMaxSize(),
                )

                is MemoUiState.Fail -> MemoScreenFail(modifier = Modifier.fillMaxSize())
            }
        }
    }

    if (memoBottomSheetState != null) {
        ModalBottomSheet(
            onDismissRequest = onMemoBottomSheetDismiss,
            sheetState = modalBottomSheetState,
        ) {
            EditMemoBottomSheetContent(
                state = memoBottomSheetState,
                onStateUpdate = onMemoBottomSheetStateUpdate,
                onDismissRequest = {
                    coroutineScope.launch { modalBottomSheetState.hide() }
                    onMemoBottomSheetDismiss()
                },
                onSubmitUpdate = { memoBottomSheetState ->
                    coroutineScope.launch { modalBottomSheetState.hide() }
                    onMemoUpdate(memoBottomSheetState)
                },
            )
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

@Composable
private fun EditMemoBottomSheetContent(
    state: MemoBottomSheetState,
    onStateUpdate: (MemoBottomSheetState) -> Unit,
    onDismissRequest: () -> Unit,
    onSubmitUpdate: (MemoBottomSheetState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val date = state.uiMemo.date

    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
        Column(
            modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TitleLarge(
                text = stringResource(id = if (state is MemoBottomSheetState.Add) R.string.memo_bottom_sheet_title_add else R.string.memo_bottom_sheet_title_edit),
                modifier = Modifier.padding(vertical = 16.dp),
            )
            HeadlineMedium(
                text = stringResource(
                    id = R.string.memo_date_format,
                    date.year, date.month, date.dayOfMonth, date.dayOfWeek.shortName,
                )
            )
            OutlinedTextField(
                value = state.uiMemo.contents,
                onValueChange = {
                    onStateUpdate(state.updateContents(it))
                },
                label = {
                    LabelMedium(text = stringResource(id = R.string.memo_bottom_sheet_textfield_label))
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                minLines = 6,
                maxLines = 6,
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() },
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )
            EditMemoBottomSheetButtons(
                onDismissRequest = {
                    focusManager.clearFocus()
                    onDismissRequest()
                },
                onSubmitUpdate = {
                    focusManager.clearFocus()
                    onSubmitUpdate(it)
                },
                state = state,
            )
        }
    }
}

@Composable
private fun EditMemoBottomSheetButtons(
    onDismissRequest: () -> Unit,
    onSubmitUpdate: (MemoBottomSheetState) -> Unit,
    state: MemoBottomSheetState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BlindarButton(
            onClick = onDismissRequest,
            isPrimary = false,
            modifier = Modifier.weight(1f),
        ) {
            BodyLarge(
                text = stringResource(id = R.string.memo_bottom_sheet_cancel_button),
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }
        BlindarButton(
            onClick = { onSubmitUpdate(state) },
            modifier = Modifier.weight(1f),
        ) {
            BodyLarge(
                text = stringResource(id = R.string.memo_bottom_sheet_submit_button),
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
            onAddMemoButtonClick = {},
            memoBottomSheetState = null,
            onMemoBottomSheetStateUpdate = {},
            onMemoUpdate = {},
            onMemoBottomSheetDismiss = {},
            onMemoDelete = {},
            onEditMemoButtonClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@DarkPreview
@Composable
private fun EditMemoBottomSheetPreview() {
    BlindarTheme {
        EditMemoBottomSheetContent(
            state = MemoBottomSheetState.Add(),
            onStateUpdate = {},
            onDismissRequest = {},
            onSubmitUpdate = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        )
    }
}