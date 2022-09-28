package com.practice.hanbitlunch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkManager
import com.example.work.setPeriodicFetchMealWork
import com.example.work.setPeriodicFetchScheduleWork
import com.practice.hanbitlunch.screen.MainScreen
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import com.practice.preferences.PreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Splash screen will dismiss as soon as the app draws its first frame
        installSplashScreen()
        setContent {
            HanbitCalendarTheme {
                MainScreen(
                    viewModel = hiltViewModel(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    enqueuePeriodicWork()
                }
            }
        }
    }

    private suspend fun enqueuePeriodicWork() {
        val isFirstExecution = preferencesRepository.fetchInitialPreferences().isFirstExecution
        if (isFirstExecution) {
            setPeriodicWork()
            preferencesRepository.updateIsFirstExecution(false)
        }
    }

    private fun setPeriodicWork() {
        val workManager = WorkManager.getInstance(this)
        setPeriodicFetchScheduleWork(workManager)
        setPeriodicFetchMealWork(workManager)
    }
}