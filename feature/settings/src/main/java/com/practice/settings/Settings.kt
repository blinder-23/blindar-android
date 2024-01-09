package com.practice.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.components.LabelMedium
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.preferences.preferences.MainScreenMode
import com.practice.settings.uistate.SettingsUiState

@Composable
fun Settings(
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    Settings(
        uiState = uiState,
        onBackButtonClick = onBackButtonClick,
        onToggleDailyMode = viewModel::onToggleDailyMode,
        onToggleDailyAlarm = viewModel::onToggleDailyAlarm,
        modifier = modifier,
    )
}

@Composable
private fun Settings(
    uiState: SettingsUiState,
    onBackButtonClick: () -> Unit,
    onToggleDailyMode: (Boolean) -> Unit,
    onToggleDailyAlarm: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is SettingsUiState.Loading -> {
            SettingsScreenLoadingIndicator(modifier = modifier)
        }

        is SettingsUiState.SettingsUiStateImpl -> {
            SettingsScreen(
                modifier = modifier,
                onBackButtonClick = onBackButtonClick,
                uiState = uiState,
                onToggleDailyMode = onToggleDailyMode,
                onToggleDailyAlarm = onToggleDailyAlarm
            )
        }
    }
}

@Composable
private fun SettingsScreenLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(0.1f)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .align(Alignment.Center),
        )
    }
}

@Composable
private fun SettingsScreen(
    uiState: SettingsUiState.SettingsUiStateImpl,
    onBackButtonClick: () -> Unit,
    onToggleDailyMode: (Boolean) -> Unit,
    onToggleDailyAlarm: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        SettingsTopAppBar(
            onBackButtonClick = onBackButtonClick,
            modifier = Modifier.fillMaxWidth(),
        )
        SettingsItems(
            uiState = uiState,
            onToggleDailyMode = onToggleDailyMode,
            onToggleDailyAlarm = onToggleDailyAlarm,
        )
        Spacer(modifier = Modifier.weight(1f))
        CloseSettingsButton(
            onClick = onBackButtonClick,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun SettingsTopAppBar(
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BlindarTopAppBar(
        title = stringResource(id = R.string.top_bar_title),
        onBackButtonClick = onBackButtonClick,
        modifier = modifier,
    )
}

@Composable
private fun SettingsItems(
    uiState: SettingsUiState.SettingsUiStateImpl,
    onToggleDailyMode: (Boolean) -> Unit,
    onToggleDailyAlarm: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DailyModeSetting(
            isDailyModeEnabled = uiState.mainScreenMode == MainScreenMode.Daily,
            onToggle = onToggleDailyMode,
        )
        DailyAlarmSetting(
            isDailyAlarmEnabled = uiState.isDailyAlarmEnabled,
            onToggle = onToggleDailyAlarm,
        )
    }
}

@Composable
private fun DailyModeSetting(
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

@Composable
private fun DailyAlarmSetting(
    isDailyAlarmEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
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
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = titleDescription
                },
            )
            LabelMedium(text = stringResource(id = R.string.settings_daily_alarm_body))
        }
        Switch(
            checked = isDailyAlarmEnabled,
            onCheckedChange = { onClick() },
            modifier = Modifier.clearAndSetSemantics { },
        )
    }
}

@Composable
private fun CloseSettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    TextButton(
        onClick = onClick,
        shape = shape,
        modifier = modifier
            .clip(shape),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        contentPadding = PaddingValues(12.dp),
    ) {
        TitleMedium(
            text = stringResource(id = R.string.close),
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@LightPreview
@Composable
private fun SettingsPreview() {
    var mainScreenMode by remember { mutableStateOf(MainScreenMode.Daily) }
    var isDailyAlarmEnabled by remember { mutableStateOf(false) }
    BlindarTheme {
        Settings(
            uiState = SettingsUiState.SettingsUiStateImpl(
                mainScreenMode = mainScreenMode,
                isDailyAlarmEnabled = isDailyAlarmEnabled,
            ),
            onBackButtonClick = {},
            onToggleDailyMode = {
                mainScreenMode = if (it) MainScreenMode.Daily else MainScreenMode.Calendar
            },
            onToggleDailyAlarm = { isDailyAlarmEnabled = it },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@LightPreview
@Composable
private fun SettingsPreview_Loading() {
    BlindarTheme {
        Settings(
            uiState = SettingsUiState.Loading,
            onBackButtonClick = {},
            onToggleDailyMode = {},
            onToggleDailyAlarm = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}