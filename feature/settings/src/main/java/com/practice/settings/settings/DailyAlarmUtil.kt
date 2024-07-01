package com.practice.settings.settings

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.practice.settings.R
import com.practice.util.makeToast

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun rememberNotificationPermissionLauncher(
    isDailyAlarmEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
): ManagedActivityResultLauncher<String, Boolean> {
    val context = LocalContext.current

    val permissionErrorMessage = stringResource(id = R.string.settings_daily_alarm_permission_error)
    val dailyAlarmEnabledMessage = stringResource(id = R.string.settings_daily_alarm_enabled)
    val onPermissionGranted = {
        onToggle(!isDailyAlarmEnabled)
        context.makeToast(dailyAlarmEnabledMessage, Toast.LENGTH_SHORT)
    }
    val onPermissionDenied = {
        context.makeToast(permissionErrorMessage, Toast.LENGTH_SHORT)
    }

    val exactAlarmPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.USE_EXACT_ALARM) { granted ->
            if (granted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    } else {
        null
    }

    return rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            exactAlarmPermissionState?.launchPermissionRequest() ?: onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }
}

internal fun onDailyAlarmClick(
    newValue: Boolean,
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    onClick: () -> Unit,
) {
    val areNotificationsEnabled = context.isNotificationAndExactAlarmEnabled()
    val toastMessageId = when (newValue) {
        true -> {
            if (!areNotificationsEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                null
            } else if (areNotificationsEnabled) {
                onClick()
                R.string.settings_daily_mode_enabled
            } else {
                null
            }
        }

        false -> {
            onClick()
            R.string.settings_daily_mode_disabled
        }
    }

    toastMessageId?.let {
        Toast.makeText(context, toastMessageId, Toast.LENGTH_SHORT).show()
    }
}

private fun Context.isNotificationAndExactAlarmEnabled(): Boolean {
    val isNotificationEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()
    val isExactAlarmEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getSystemService<AlarmManager>()!!.canScheduleExactAlarms()
    } else {
        true
    }
    return isNotificationEnabled && isExactAlarmEnabled
}