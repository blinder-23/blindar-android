package com.practice.main.state

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.ui.graphics.vector.ImageVector
import com.practice.main.R

enum class DailyAlarmIconState(
    val icon: ImageVector,
    @StringRes val iconDescriptionId: Int,
) {
    Loading(
        icon = Icons.Default.MoreHoriz,
        iconDescriptionId = R.string.daily_alarm_icon_loading,
    ),
    Enabled(
        icon = Icons.Default.Notifications,
        iconDescriptionId = R.string.daily_alarm_icon_enabled,
    ),
    Disabled(
        icon = Icons.Default.NotificationsOff,
        iconDescriptionId = R.string.daily_alarm_icon_disabled,
    )
}