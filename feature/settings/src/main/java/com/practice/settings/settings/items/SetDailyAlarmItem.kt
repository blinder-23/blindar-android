package com.practice.settings.settings.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.LabelMedium
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.settings.R
import com.practice.settings.settings.onDailyAlarmClick
import com.practice.settings.settings.rememberNotificationPermissionLauncher

@Composable
internal fun SetDailyAlarmItem(
    isDailyAlarmEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    val permissionLauncher = rememberNotificationPermissionLauncher(isDailyAlarmEnabled, onToggle)

    val context = LocalContext.current
    val onClick = {
        onDailyAlarmClick(!isDailyAlarmEnabled, context, permissionLauncher) {
            onToggle(!isDailyAlarmEnabled)
        }
    }

    val titleDescription =
        stringResource(id = if (isDailyAlarmEnabled) R.string.settings_daily_alarm_title_description_enabled else R.string.settings_daily_alarm_title_description_disabled)
    val onClickLabel =
        stringResource(id = if (isDailyAlarmEnabled) R.string.settings_daily_alarm_disable else R.string.settings_daily_alarm_enable)

    Row(
        modifier = modifier
            .semantics(mergeDescendants = true) {}
            .clickable(
                onClick = onClick,
                onClickLabel = onClickLabel,
                role = Role.Switch,
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TitleMedium(
                text = stringResource(id = R.string.settings_daily_alarm_title),
                color = color,
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = titleDescription
                },
            )
            LabelMedium(
                text = stringResource(id = R.string.settings_daily_alarm_body),
                color = color,
            )
        }
        Switch(
            checked = isDailyAlarmEnabled,
            onCheckedChange = { onClick() },
            modifier = Modifier.clearAndSetSemantics { },
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SetDailyAlarmItemPreview() {
    var enabled by remember { mutableStateOf(false) }
    BlindarTheme {
        SetDailyAlarmItem(isDailyAlarmEnabled = enabled, onToggle = { enabled = !it })
    }
}