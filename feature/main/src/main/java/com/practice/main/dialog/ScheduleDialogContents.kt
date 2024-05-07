package com.practice.main.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hsk.ktx.date.Date
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.ScheduleContent
import com.practice.main.previewMemos
import com.practice.main.previewSchedules
import com.practice.main.state.MemoDialogElement
import com.practice.main.state.UiMemos
import com.practice.main.state.UiSchedules
import com.practice.main.state.mergeSchedulesAndMemos
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ScheduleDialogContents(
    scheduleElements: ImmutableList<MemoDialogElement>,
    onMemoDialogOpen: () -> Unit,
    onScheduleDialogClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .shadow(4.dp, shape = shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ScheduleContent(
            scheduleElements = scheduleElements,
            onMemoDialogOpen = onMemoDialogOpen,
        )
        CloseScheduleDialogButton(
            onClose = onScheduleDialogClose,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Composable
private fun CloseScheduleDialogButton(
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
            text = stringResource(id = R.string.schedule_dialog_close),
            color = textColor,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ScheduleDialogContentsPreview() {
    BlindarTheme {
        ScheduleDialogContents(
            scheduleElements = mergeSchedulesAndMemos(
                UiSchedules(
                    date = Date.now(),
                    uiSchedules = previewSchedules,
                ),
                UiMemos(
                    date = Date.now(),
                    memos = previewMemos,
                ),
            ),
            onMemoDialogOpen = {},
            onScheduleDialogClose = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}