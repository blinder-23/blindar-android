package com.practice.hanbitlunch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkManager
import com.example.work.setOneTimeFetchMealWork
import com.example.work.setOneTimeFetchScheduleWork
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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Splash screen will dismiss as soon as the app draws its first frame
        installSplashScreen()
        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            HanbitCalendarTheme {
                MainScreen(
                    windowSize = windowSizeClass,
                    viewModel = hiltViewModel(),
                    modifier = Modifier.fillMaxSize(),
                    onLaunch = ::enqueuePeriodicWork,
                    onRefresh = ::setOneTimeWork,
                )
            }
        }
    }

    private suspend fun enqueuePeriodicWork() {
        setPeriodicWork()
        preferencesRepository.updateIsFirstExecution(false)
    }

    private fun setPeriodicWork() {
        val workManager = WorkManager.getInstance(this)
        setPeriodicFetchScheduleWork(workManager)
        setPeriodicFetchMealWork(workManager)
    }

    private fun setOneTimeWork() {
        val workManager = WorkManager.getInstance(this)
        setOneTimeFetchScheduleWork(workManager)
        setOneTimeFetchMealWork(workManager)
    }
}