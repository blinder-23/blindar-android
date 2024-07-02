package com.practice.settings.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BlindarChip
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.components.BlindarTopAppBarDefaults
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.preferences.preferences.MainScreenMode
import com.practice.settings.R
import com.practice.settings.settings.dialog.LogoutDialog
import com.practice.settings.settings.dialog.MainScreenModeDialog
import com.practice.settings.settings.items.SendFeedbackItem
import com.practice.settings.settings.items.SetDailyAlarmItem
import com.practice.settings.settings.items.SetDailyModeItem
import com.practice.settings.settings.uistate.ProfileUiState
import com.practice.settings.settings.uistate.SettingsUiState

@Composable
fun Settings(
    onBackButtonClick: () -> Unit,
    onLogout: () -> Unit,
    onFeedbackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isScreenModeDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isFeedbackDialogVisible by rememberSaveable { mutableStateOf(false) }

    val isLogoutDialogOpen by viewModel.logoutDialogOpen.collectAsStateWithLifecycle()

    Settings(
        uiState = uiState,
        onBackButtonClick = onBackButtonClick,
        isLogoutDialogVisible = isLogoutDialogOpen,
        onLogoutDialogOpen = viewModel::onLogoutDialogOpen,
        onLogoutDialogDismiss = viewModel::onLogoutDialogDismiss,
        onLogout = {
            viewModel.onLogout()
            onLogout()
        },
        isScreenModeDialogVisible = isScreenModeDialogVisible,
        onChangeScreenModeDialogVisibility = {
            isScreenModeDialogVisible = it
        },
        onToggleDailyMode = viewModel::onToggleDailyMode,
        onToggleDailyAlarm = viewModel::onToggleDailyAlarm,
        isFeedbackDialogVisible = isFeedbackDialogVisible,
        onFeedbackButtonClick = onFeedbackButtonClick,
        onSendFeedback = viewModel::sendFeedback,
        modifier = modifier,
    )
}

@Composable
private fun Settings(
    uiState: SettingsUiState,
    onBackButtonClick: () -> Unit,
    isLogoutDialogVisible: Boolean,
    onLogoutDialogOpen: () -> Unit,
    onLogoutDialogDismiss: () -> Unit,
    onLogout: () -> Unit,
    isScreenModeDialogVisible: Boolean,
    onChangeScreenModeDialogVisibility: (Boolean) -> Unit,
    onToggleDailyMode: (Boolean) -> Unit,
    onToggleDailyAlarm: (Boolean) -> Unit,
    isFeedbackDialogVisible: Boolean,
    onFeedbackButtonClick: () -> Unit,
    onSendFeedback: suspend (String, String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val keepDailyModeMessage =
        stringResource(id = R.string.screen_mode_dialog_keep_daily_mode_toast)
    val setDailyModeMessage = stringResource(id = R.string.screen_mode_dialog_set_daily_mode_toast)
    val setCalendarModeMessage = stringResource(id = R.string.settings_daily_mode_disabled)

    val showToggleToast = { toggleMessage: String ->
        Toast.makeText(context, toggleMessage, Toast.LENGTH_SHORT).show()
    }

    when (uiState) {
        is SettingsUiState.Loading -> {
            SettingsScreenLoadingIndicator(modifier = modifier)
        }

        is SettingsUiState.SettingsUiStateImpl -> {
            SettingsScreen(
                modifier = modifier,
                onBackButtonClick = onBackButtonClick,
                uiState = uiState,
                onLogoutDialogOpen = onLogoutDialogOpen,
                onToggleDailyMode = {
                    if (uiState.mainScreenMode == MainScreenMode.Calendar) {
                        onToggleDailyMode(true)
                        showToggleToast(setDailyModeMessage)
                    } else {
                        onChangeScreenModeDialogVisibility(true)
                    }
                },
                onToggleDailyAlarm = onToggleDailyAlarm,
                onFeedbackButtonClick = onFeedbackButtonClick,
            )
        }
    }

    if (isScreenModeDialogVisible) {
        MainScreenModeDialog(
            onScreenModeSet = { newScreenMode ->
                val isNewModeDaily = newScreenMode == MainScreenMode.Daily
                val toastMessage =
                    if (isNewModeDaily) keepDailyModeMessage else setCalendarModeMessage

                onToggleDailyMode(isNewModeDaily)
                showToggleToast(toastMessage)
                onChangeScreenModeDialogVisibility(false)
            },
            onDismiss = {
                showToggleToast(keepDailyModeMessage)
                onChangeScreenModeDialogVisibility(false)
            },
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp),
        )
    }

    if (isLogoutDialogVisible) {
        LogoutDialog(
            onDismiss = onLogoutDialogDismiss,
            onLogout = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp),
        )
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
    onLogoutDialogOpen: () -> Unit,
    onToggleDailyMode: (Boolean) -> Unit,
    onToggleDailyAlarm: (Boolean) -> Unit,
    onFeedbackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        SettingsTopAppBar(
            onBackButtonClick = onBackButtonClick,
            modifier = Modifier.fillMaxWidth(),
        )
        UserProfile(
            onLogout = onLogoutDialogOpen,
            profileUiState = uiState.profileUiState,
            modifier = Modifier.fillMaxWidth(),
        )
        SettingsItems(
            uiState = uiState,
            onToggleDailyMode = onToggleDailyMode,
            onToggleDailyAlarm = onToggleDailyAlarm,
            onFeedbackButtonClick = onFeedbackButtonClick,
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
        modifier = modifier,
        navigationIcon = {
            BlindarTopAppBarDefaults.NavigationIcon(onClick = onBackButtonClick)
        },
    )
}

@Composable
private fun UserProfile(
    onLogout: () -> Unit,
    profileUiState: ProfileUiState,
    modifier: Modifier = Modifier,
) {
    val usernameDescription =
        stringResource(id = R.string.settings_username, profileUiState.username)
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            SubcomposeAsyncImage(
                model = profileUiState.profileImageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                error = {
                    Icon(
                        imageVector = Icons.Filled.QuestionMark,
                        contentDescription = null,
                    )
                },
            )
            BodyLarge(
                text = profileUiState.username,
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = usernameDescription
                },
            )
            Spacer(modifier = Modifier.weight(1f))
            BlindarChip(
                text = stringResource(id = R.string.settings_logout),
                enabled = true,
                onClick = onLogout,
            )
        }
    }
}

@Composable
private fun SettingsItems(
    uiState: SettingsUiState.SettingsUiStateImpl,
    onToggleDailyMode: (Boolean) -> Unit,
    onToggleDailyAlarm: (Boolean) -> Unit,
    onFeedbackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.Unspecified,
) {
    val textColor = contentColor.takeOrElse {
        MaterialTheme.colorScheme.onSurface
    }
    Column(modifier = modifier) {
        SetDailyModeItem(
            isDailyModeEnabled = uiState.mainScreenMode == MainScreenMode.Daily,
            onToggle = onToggleDailyMode,
            color = textColor,
        )
        SetDailyAlarmItem(
            isDailyAlarmEnabled = uiState.isDailyAlarmEnabled,
            onToggle = onToggleDailyAlarm,
            color = textColor,
        )
        SendFeedbackItem(
            onClick = onFeedbackButtonClick,
            color = textColor,
        )
    }
}

@LightPreview
@Composable
private fun SettingsPreview() {
    var mainScreenMode by remember { mutableStateOf(MainScreenMode.Daily) }
    var isDailyAlarmEnabled by remember { mutableStateOf(false) }
    var isScreenModeDialogVisible by remember { mutableStateOf(false) }
    var isFeedbackDialogVisible by remember { mutableStateOf(false) }

    BlindarTheme {
        Settings(
            uiState = SettingsUiState.SettingsUiStateImpl(
                mainScreenMode = mainScreenMode,
                isDailyAlarmEnabled = isDailyAlarmEnabled,
                profileUiState = ProfileUiState(
                    profileImageUri = null,
                    username = "블린더",
                ),
            ),
            onBackButtonClick = {},
            isLogoutDialogVisible = false,
            onLogoutDialogOpen = {},
            onLogoutDialogDismiss = {},
            onLogout = {},
            isScreenModeDialogVisible = isScreenModeDialogVisible,
            onChangeScreenModeDialogVisibility = {
                isScreenModeDialogVisible = it
            },
            onToggleDailyMode = {
                mainScreenMode = if (it) MainScreenMode.Daily else MainScreenMode.Calendar
            },
            onToggleDailyAlarm = { isDailyAlarmEnabled = it },
            isFeedbackDialogVisible = isFeedbackDialogVisible,
            onFeedbackButtonClick = {},
            onSendFeedback = { _, _ -> true },
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
            isLogoutDialogVisible = false,
            onLogoutDialogOpen = {},
            onLogoutDialogDismiss = {},
            onLogout = {},
            isScreenModeDialogVisible = false,
            onChangeScreenModeDialogVisibility = {},
            onToggleDailyMode = {},
            onToggleDailyAlarm = {},
            isFeedbackDialogVisible = false,
            onFeedbackButtonClick = {},
            onSendFeedback = { _, _ -> true },
            modifier = Modifier.fillMaxSize(),
        )
    }
}