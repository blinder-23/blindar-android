package com.practice.hanbitlunch.screen.main.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

data class ScreenModeIcon(
    val screenMode: com.practice.preferences.ScreenMode,
    val icon: ImageVector
)

internal val screenModeIcons = listOf(
    ScreenModeIcon(com.practice.preferences.ScreenMode.Default, Icons.Filled.Event),
    ScreenModeIcon(com.practice.preferences.ScreenMode.List, Icons.Filled.List),
)