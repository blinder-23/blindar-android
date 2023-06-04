package com.practice.hanbitlunch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.practice.designsystem.theme.BlindarTheme
import com.practice.hanbitlunch.screen.BlindarNavHost
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
            BlindarTheme {
                BlindarNavHost(
                    windowSizeClass = windowSizeClass,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}