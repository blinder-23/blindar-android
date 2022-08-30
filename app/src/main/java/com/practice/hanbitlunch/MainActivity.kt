package com.practice.hanbitlunch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.mwy3055.violetdreams.core.theme.VioletDreamsTheme
import com.practice.hanbitlunch.screen.MainScreen
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Splash screen will dismiss as soon as the app draws its first frame
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            HanbitCalendarTheme {
                MainScreen(
                    viewModel = hiltViewModel(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    WindowCompat.setDecorFitsSystemWindows(window, true)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VioletDreamsTheme {
        Greeting("Android")
    }
}