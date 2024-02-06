package com.practice.settings.items

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    modifier: Modifier = Modifier
) {
    val titleDescription =
        stringResource(id = if (isDailyModeEnabled) R.string.settings_daily_mode_title_description_enabled else R.string.settings_daily_mode_title_description_disabled)
    val onClickLabel =
        stringResource(id = if (isDailyModeEnabled) R.string.settings_daily_mode_disable else R.string.settings_daily_mode_enable)
    val onToggleMessage =
        stringResource(id = if (isDailyModeEnabled) R.string.settings_daily_mode_disabled else R.string.settings_daily_mode_enabled)

    val context = LocalContext.current
    val afterToggle = {
        Toast.makeText(context, onToggleMessage, Toast.LENGTH_SHORT).show()
    }

    Row(
        modifier = modifier
            .semantics(mergeDescendants = true) {}
            .clickable(
                onClick = {
                    onToggle(!isDailyModeEnabled)
                    afterToggle()
                },
                onClickLabel = onClickLabel,
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
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = titleDescription
                },
            )
            LabelMedium(text = stringResource(id = R.string.settings_daily_mode_body))
        }
        Switch(
            checked = isDailyModeEnabled,
            onCheckedChange = {
                onToggle(!isDailyModeEnabled)
                afterToggle()
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