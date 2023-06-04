package com.practice.hanbitlunch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.WorkManager
import com.practice.designsystem.theme.BlindarTheme
import com.practice.hanbitlunch.screen.BlindarNavHost
import com.practice.preferences.PreferencesRepository
import com.practice.work.setOneTimeFetchMealWork
import com.practice.work.setOneTimeFetchScheduleWork
import com.practice.work.setPeriodicFetchMealWork
import com.practice.work.setPeriodicFetchScheduleWork
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
            BlindarTheme {
                BlindarNavHost(
                    windowSizeClass = windowSizeClass,
                    onMainScreenLaunch = ::enqueuePeriodicWork,
                    onMainScreenRefresh = ::setOneTimeWork,
                    modifier = Modifier.fillMaxSize(),
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