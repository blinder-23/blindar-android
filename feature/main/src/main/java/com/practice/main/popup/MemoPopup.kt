package com.practice.main.popup

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.LabelLarge
import com.practice.designsystem.components.PopupBodySmall
import com.practice.designsystem.components.PopupTitleLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.state.MemoPopupElement
import com.practice.main.state.UiMemo
import com.practice.main.state.UiSchedule
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MemoPopup(
    date: Date,
    memoPopupElements: ImmutableList<MemoPopupElement>,
    onAddMemo: () -> Unit,
    onContentsChange: (UiMemo) -> Unit,
    onMemoDelete: (UiMemo) -> Unit,
    onPopupClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface)

    val shape = RoundedCornerShape(16.dp)
    LazyColumn(
        modifier = modifier
            .shadow(4.dp, shape = shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .heightIn(max = 550.dp),
    ) {
        item {
            val (_, month, day) = date
            PopupTitleLarge(
                text = stringResource(id = R.string.memo_popup_title, month, day),
                color = textColor,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            )
        }
        item {
            MemoItems(
                memoPopupElements = memoPopupElements,
                onContentsChange = onContentsChange,
                onMemoDelete = onMemoDelete,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
            )
        }
        item {
            AddMemoButton(
                textColor = textColor,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clickable { onAddMemo() },
            )
        }
        item {
            CloseMemoPopupButton(
                onClose = onPopupClose,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun MemoItems(
    memoPopupElements: ImmutableList<MemoPopupElement>,
    onContentsChange: (UiMemo) -> Unit,
    onMemoDelete: (UiMemo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        memoPopupElements.forEach { element ->
            when (element) {
                is UiMemo -> MemoItem(
                    memo = element,
                    onContentsChange = onContentsChange,
                    onMemoDelete = onMemoDelete,
                    modifier = Modifier.fillMaxWidth(),
                )

                is UiSchedule -> ScheduleItem(
                    uiSchedule = element,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ScheduleItem(
    uiSchedule: UiSchedule,
    modifier: Modifier = Modifier,
) {
    PopupElementItem(
        readOnly = true,
        text = uiSchedule.displayText,
        onTextChange = {},
        label = null,
        trailingIcon = null,
        modifier = modifier
    )
}

@Composable
private fun MemoItem(
    memo: UiMemo,
    onContentsChange: (UiMemo) -> Unit,
    onMemoDelete: (UiMemo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Log.d("UI_MEMO", "contents: ${memo.contents}")
    PopupElementItem(
        readOnly = false,
        text = memo.contents,
        onTextChange = { onContentsChange(memo.copy(contents = it)) },
        label = {
            PopupBodySmall(
                text = stringResource(id = R.string.memo_popup_item_label),
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            )
        },
        trailingIcon = {
            IconButton(onClick = { onMemoDelete(memo) }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(
                        id = R.string.memo_popup_item_delete,
                        memo.contents,
                    ),
                    tint = contentColorFor(MaterialTheme.colorScheme.surface),
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun PopupElementItem(
    readOnly: Boolean,
    text: String,
    onTextChange: (String) -> Unit,
    label: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            readOnly = readOnly,
            value = text,
            onValueChange = onTextChange,
            label = label,
            modifier = Modifier.weight(1f),
            singleLine = true,
            trailingIcon = if (!readOnly && text.isNotEmpty()) {
                {
                    IconButton(onClick = { onTextChange("") }) {
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = stringResource(
                                id = R.string.memo_popup_clear_text,
                                text
                            ),
                        )
                    }
                }
            } else {
                null
            },
        )
        trailingIcon?.invoke()
    }
}

@Composable
private fun AddMemoButton(
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val a11yDescription = stringResource(id = R.string.memo_popup_add_memo_description)
    Box(
        modifier = modifier.semantics {
            role = Role.Button
        }
    ) {
        LabelLarge(
            text = stringResource(id = R.string.memo_popup_add_memo),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 16.dp)
                .semantics {
                    contentDescription = a11yDescription
                },
            color = textColor,
        )
    }
}

@Composable
private fun CloseMemoPopupButton(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = MaterialTheme.colorScheme.primaryContainer
    val textColor = contentColorFor(backgroundColor = background)
    Box(
        modifier = modifier
            .clickable { onClose() }
            .background(background),
    ) {
        BodyLarge(
            text = stringResource(id = R.string.memo_popup_close),
            color = textColor,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center),
        )
    }
}

private val previewMemoPopupElements: ImmutableList<MemoPopupElement> = (1..4).map {
    if (it <= 2) {
        UiSchedule(
            schoolCode = 1,
            id = it.toLong(),
            year = 2023,
            month = 11,
            day = 13,
            eventName = "test $it",
            eventContent = "content $it",
        )
    } else {
        UiMemo(
            id = "test $it",
            userId = "test",
            year = 2023,
            month = 11,
            day = 13,
            contents = "test $it",
            isSavedOnRemote = false,
        )
    }
}.toImmutableList()

@LightAndDarkPreview
@Composable
private fun MemoItemsPreview() {
    BlindarTheme {
        MemoItems(
            memoPopupElements = previewMemoPopupElements,
            onContentsChange = {},
            onMemoDelete = {},
            modifier = Modifier
                .wrapContentWidth()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun MemoPopupPreview() {
    BlindarTheme {
        MemoPopup(
            date = Date.now(),
            memoPopupElements = previewMemoPopupElements,
            onContentsChange = {},
            onMemoDelete = {},
            onPopupClose = {},
            onAddMemo = {},
            modifier = Modifier
                .width(350.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}