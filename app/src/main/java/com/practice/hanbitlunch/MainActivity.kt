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
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.practice.designsystem.theme.BlindarTheme
import com.practice.firebase.R
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
                        signInWithGoogleRequest = getSignInWithGoogleRequest(),
                        modifier = Modifier
//                            .safeDrawingPadding()
                            .fillMaxSize(),
                        onNavigateToMainScreen = {
//                            WindowCompat.setDecorFitsSystemWindows(window, true)
                        }
                    )
                }
            }
        }
    }

    private fun getSignInWithGoogleRequest(): GetCredentialRequest {
        val webClientId = getString(R.string.web_client_id)
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(webClientId)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()
        return request
    }
}