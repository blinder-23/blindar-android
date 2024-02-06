package com.practice.main.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.components.PopupBodyMedium
import com.practice.designsystem.components.PopupTitleLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.main.R
import com.practice.main.state.MainUiMode
import com.practice.util.makeToast

@Composable
fun MainScreenModePopup(
    onScreenModeSet: (MainUiMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier
            .shadow(4.dp, shape = shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        MainScreenModePopupContents(modifier = Modifier.padding(16.dp))
        MainScreenModeButtons(
            onScreenModeSet = onScreenModeSet,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun MainScreenModePopupContents(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MainScreenModePopupTitle()
        MainScreenModePopupBody()
    }
}

@Composable
private fun MainScreenModePopupTitle(
    modifier: Modifier = Modifier,
) {
    PopupTitleLarge(
        text = stringResource(id = R.string.ui_mode_popup_title),
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun MainScreenModePopupBody(
    modifier: Modifier = Modifier,
) {
    PopupBodyMedium(
        text = stringResource(id = R.string.ui_mode_popup_body),
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun MainScreenModeButtons(
    onScreenModeSet: (MainUiMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        MainScreenDailyModeButton(
            onDailyModeSet = { onScreenModeSet(MainUiMode.DAILY) },
            modifier = Modifier.weight(1f),
        )
        MainScreenCalendarModeButton(
            onCalendarModeSet = { onScreenModeSet(MainUiMode.CALENDAR) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MainScreenDailyModeButton(
    onDailyModeSet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val toastMessage = stringResource(id = R.string.ui_mode_popup_daily_mode_set)

    BaseMainScreenModePopupButton(
        text = stringResource(id = R.string.ui_mode_popup_use_daily),
        onClick = {
            onDailyModeSet()
            context.makeToast(toastMessage)
        },
        modifier = modifier,
        background = MaterialTheme.colorScheme.surface,
    )
}

@Composable
private fun MainScreenCalendarModeButton(
    onCalendarModeSet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val toastMessage = stringResource(id = R.string.ui_mode_popup_calendar_mode_set)

    BaseMainScreenModePopupButton(
        text = stringResource(id = R.string.ui_mode_popup_use_calendar),
        onClick = {
            onCalendarModeSet()
            context.makeToast(toastMessage)
        },
        modifier = modifier,
        background = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun BaseMainScreenModePopupButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    val textColor = contentColorFor(backgroundColor = background)
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Button
            }
            .background(background),
    ) {
        BodyLarge(
            text = text,
            textColor = textColor,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun MainScreenModePopupPreview() {
    BlindarTheme {
        MainScreenModePopup(
            onScreenModeSet = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}