package com.practice.main.memo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.modifier.drawBottomBorder
import com.practice.designsystem.modifier.drawTopBorder
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.state.UiMemo
import com.practice.main.state.UiSchedule

@Composable
fun ScheduleItem(
    item: UiSchedule,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .drawTopBorder(MaterialTheme.colorScheme.surfaceVariant, width = 1.dp)
            .drawBottomBorder(MaterialTheme.colorScheme.surfaceVariant, width = 1.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        BodyLarge(
            text = item.displayText,
            modifier = Modifier.minimumInteractiveComponentSize(),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun MemoItem(
    item: UiMemo,
    onEditButtonClick: (UiMemo) -> Unit,
    onDeleteButtonClick: (UiMemo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    Row(
        modifier = modifier
            .background(background)
            .drawTopBorder(MaterialTheme.colorScheme.surfaceVariant, width = 1.dp)
            .drawBottomBorder(MaterialTheme.colorScheme.surfaceVariant, width = 1.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BodyLarge(
            text = item.displayText,
            color = contentColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f),
        )
        Row {
            IconButton(onClick = { onEditButtonClick(item) }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(id = R.string.memo_item_edit),
                    tint = contentColor,
                )
            }
            IconButton(onClick = { onDeleteButtonClick(item) }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(id = R.string.memo_item_delete),
                    tint = contentColor,
                )
            }
        }
    }
}

@DarkPreview
@Composable
private fun ScheduleItemPreview() {
    BlindarTheme {
        ScheduleItem(
            item = UiSchedule(
                schoolCode = 123,
                id = 1L,
                year = 2024,
                month = 6,
                day = 25,
                eventName = "다람쥐 헌 쳇바퀴에 타고파",
                eventContent = "미리보기",
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@DarkPreview
@Composable
private fun MemoItemPreview() {
    val item = UiMemo(
        id = "test",
        userId = "test",
        year = 2024,
        month = 6,
        day = 25,
        contents = "dtd",
        isSavedOnRemote = true,
    )
    BlindarTheme {
        Column {
            MemoItem(
                item = item,
                onEditButtonClick = {},
                onDeleteButtonClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
            MemoItem(
                item = item.copy(contents = item.contents.repeat(50)),
                onEditButtonClick = {},
                onDeleteButtonClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}