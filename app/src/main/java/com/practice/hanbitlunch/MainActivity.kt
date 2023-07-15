package com.practice.hanbitlunch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.practice.designsystem.theme.BlindarTheme
import com.practice.firebase.R
import com.practice.hanbitlunch.screen.BlindarNavHost
import com.practice.preferences.PreferencesRepository
import com.practice.util.LocalActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Splash screen will dismiss as soon as the app draws its first frame
        installSplashScreen()
        setContent {
            CompositionLocalProvider(LocalActivity provides this) {
                val windowSizeClass = calculateWindowSizeClass(activity = this)
                BlindarTheme {
                    BlindarNavHost(
                        windowSizeClass = windowSizeClass,
                        modifier = Modifier.fillMaxSize(),
                        googleSignInClient = getGoogleSignInClient(),
                    )
                }
            }
        }
    }

    private fun getGoogleSignInClient(): GoogleSignInClient {
        val webClientId = getString(R.string.web_client_id)
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, options)
    }
}