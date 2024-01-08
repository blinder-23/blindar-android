package com.practice.main.daily

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.practice.designsystem.components.TitleLarge
import com.practice.main.MainScreenViewModel
import com.practice.main.mealColumns

@Composable
fun DailyMainScreen(
    windowSize: WindowSizeClass,
    viewModel: MainScreenViewModel,
    onNavigateToSettingsScreen: () -> Unit,
    onNavigateToSelectSchoolScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState

    val mealColumns = windowSize.mealColumns

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> {

        }

        else -> {
            // TODO: Vertical Daily Main Screen
            TitleLarge(text = "TODO!")
        }
    }
}