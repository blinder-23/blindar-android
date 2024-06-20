package com.practice.main.main.daily.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.a11y.isLargeFont
import com.practice.designsystem.components.BodySmall
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.main.daily.picker.DailyDatePickerState
import com.practice.main.main.daily.picker.rememberDailyDatePickerState
import kotlin.math.absoluteValue

@Composable
internal fun DateQuickNavigationButtons(
    datePickerState: DailyDatePickerState,
    modifier: Modifier = Modifier,
    navigationElements: Collection<DateQuickNavigation> = DateQuickNavigation.entries,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        navigationElements.forEach {
            DateQuickNavigationButton(
                datePickerState = datePickerState,
                quickNavigation = it,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
        }
    }
}

@Composable
internal fun DateQuickNavigationButton(
    datePickerState: DailyDatePickerState,
    quickNavigation: DateQuickNavigation,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(6.dp)
    val backgroundColor = MaterialTheme.colorScheme.surface
    Box(
        modifier = modifier
            .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, shape)
            .background(backgroundColor, shape)
            .clickable(onClickLabel = stringResource(id = quickNavigation.descriptionId)) {
                if (quickNavigation == DateQuickNavigation.TODAY) {
                    datePickerState.setToday()
                } else if (quickNavigation.daysToMove > 0) {
                    datePickerState.plusDays(quickNavigation.daysToMove)
                } else if (quickNavigation.daysToMove < 0) {
                    datePickerState.minusDays(quickNavigation.daysToMove.absoluteValue)
                }
            }
            .semantics {
                role = Role.Button
            }
            .padding(12.dp)
    ) {
        BodySmall(
            text = stringResource(id = quickNavigation.nameId),
            modifier = Modifier.align(Alignment.Center),
            color = contentColorFor(backgroundColor = backgroundColor),
            fontWeight = if (LocalDensity.current.isLargeFont) FontWeight.Bold else null,
        )
    }
}

@Composable
internal fun QuickViewDialogButtons(
    isMealDialogEnabled: Boolean,
    onMealDialogOpen: () -> Unit,
    isScheduleDialogEnabled: Boolean,
    onScheduleDialogOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        QuickViewDialogButton(
            enabled = isMealDialogEnabled,
            description = stringResource(id = if (isMealDialogEnabled) R.string.open_meal_dialog else R.string.meal_dialog_unavailable),
            onOpenDialog = onMealDialogOpen,
        )
        QuickViewDialogButton(
            enabled = isScheduleDialogEnabled,
            description = stringResource(id = if (isScheduleDialogEnabled) R.string.open_schedule_dialog else R.string.schedule_dialog_unavailable),
            onOpenDialog = onScheduleDialogOpen,
        )
    }
}

@Composable
private fun QuickViewDialogButton(
    enabled: Boolean,
    description: String,
    onOpenDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .semantics {
                role = Role.Button
                contentDescription = description
            }
            .clickable(enabled = enabled, onClick = onOpenDialog)
            .size(8.dp)
            .background(Color.Transparent),
    )
}

@DarkPreview
@Composable
private fun DateQuickNavigationButtonsPreview() {
    val datePickerState = rememberDailyDatePickerState()
    BlindarTheme {
        DateQuickNavigationButtons(
            datePickerState = datePickerState,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

@Preview(fontScale = 2f, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DateQuickNavigationButtonsPreview_Test() {
    val datePickerState = rememberDailyDatePickerState()
    BlindarTheme {
        DateQuickNavigationButtons(
            datePickerState = datePickerState,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}