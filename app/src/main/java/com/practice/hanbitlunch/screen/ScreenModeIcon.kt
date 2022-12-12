package com.practice.hanbitlunch.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.practice.preferences.ScreenMode

data class ScreenModeIcon(
    val screenMode: ScreenMode,
    val icon: ImageVector
)

internal val screenModeIcons = listOf(
    ScreenModeIcon(ScreenMode.Default, Icons.Filled.Event),
    ScreenModeIcon(ScreenMode.Calendar, Icons.Filled.EventNote),
    ScreenModeIcon(ScreenMode.List, Icons.Filled.List),
)