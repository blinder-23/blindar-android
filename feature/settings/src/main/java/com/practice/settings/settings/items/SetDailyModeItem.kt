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

@Composable
internal fun SetDailyModeItem(
    isDailyModeEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    val titleDescription =
        stringResource(id = if (isDailyModeEnabled) R.string.settings_daily_mode_title_description_enabled else R.string.settings_daily_mode_title_description_disabled)
    val onClickLabel =
        stringResource(id = if (isDailyModeEnabled) R.string.settings_daily_mode_disable else R.string.settings_daily_mode_enable)

    Row(
        modifier = modifier
            .semantics(mergeDescendants = true) {}
            .clickable(
                onClick = {
                    onToggle(!isDailyModeEnabled)
                },
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
                text = stringResource(id = R.string.settings_daily_mode_title),
                color = color,
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = titleDescription
                },
            )
            LabelMedium(
                text = stringResource(id = R.string.settings_daily_mode_body),
                color = color,
            )
        }
        Switch(
            checked = isDailyModeEnabled,
            onCheckedChange = {
                onToggle(!isDailyModeEnabled)
            },
            modifier = Modifier.clearAndSetSemantics { },
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SetDailyModeItemPreview() {
    var enabled by remember { mutableStateOf(false) }
    BlindarTheme {
        SetDailyModeItem(isDailyModeEnabled = enabled, onToggle = { enabled = !it })
    }
}