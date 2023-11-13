package com.practice.main.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.LabelLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.state.UiMemo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MemoPopup(
    memos: ImmutableList<UiMemo>,
    onContentsChange: (UiMemo) -> Unit,
    onMemoDelete: (String) -> Unit,
    onPopupClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.onSurface)
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        item {
            PopupTitleLarge(
                text = stringResource(id = R.string.memo_popup_title),
                color = textColor,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            )
        }
        item {
            MemoItems(
                memos = memos,
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
                    .clickable {},
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
    memos: ImmutableList<UiMemo>,
    onContentsChange: (UiMemo) -> Unit,
    onMemoDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        memos.forEach { memo ->
            MemoItem(
                memo = memo,
                onContentsChange = onContentsChange,
                onMemoDelete = onMemoDelete,
            )
        }
    }
}

@Composable
private fun MemoItem(
    memo: UiMemo,
    onContentsChange: (UiMemo) -> Unit,
    onMemoDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = memo.contents,
            onValueChange = { onContentsChange(memo.copy(contents = it)) },
            label = {
                PopupBodySmall(
                    text = stringResource(id = R.string.memo_popup_item_label),
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                )
            },
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { onMemoDelete(memo.id) }) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(
                    id = R.string.memo_popup_item_delete,
                    memo.contents,
                ),
                tint = contentColorFor(MaterialTheme.colorScheme.surface),
            )
        }
    }
}

@Composable
private fun AddMemoButton(
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LabelLarge(
            text = stringResource(id = R.string.memo_popup_add_memo),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 16.dp),
            textColor = textColor,
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
            textColor = textColor,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center),
        )
    }
}

private val previewMemos = (1..4).map {
    UiMemo(
        id = "test $it",
        userId = "test",
        year = 2023,
        month = 11,
        day = 13,
        contents = "test $it",
    )
}.toImmutableList()

@LightAndDarkPreview
@Composable
private fun MemoItemsPreview() {
    BlindarTheme {
        MemoItems(
            memos = previewMemos,
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
private fun MemoItemPreview() {
    BlindarTheme {
        MemoItem(
            memo = previewMemos[0],
            onContentsChange = { },
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
            memos = previewMemos,
            onContentsChange = {},
            onMemoDelete = {},
            onPopupClose = {},
            modifier = Modifier
                .width(350.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}