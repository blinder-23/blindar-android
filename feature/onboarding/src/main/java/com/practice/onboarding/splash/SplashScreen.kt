package com.practice.onboarding.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.theme.BlindarTheme
import com.practice.designsystem.theme.NanumSquareRound

@Composable
fun SplashScreen(
    login: suspend () -> Boolean,
    onAutoLoginSuccess: () -> Unit,
    onAutoLoginFail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colorScheme.surface
    LaunchedEffect(true) {
        systemUiController.setSystemBarsColor(systemBarColor)
        if (login()) {
            onAutoLoginSuccess()
        } else {
            onAutoLoginFail()
        }
    }

    ConstraintLayout(modifier = modifier) {
        val (text) = createRefs()
        Text(
            text = "Splash!",
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            fontFamily = NanumSquareRound,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SplashScreenPreview() {
    BlindarTheme {
        SplashScreen(
            login = { true },
            onAutoLoginSuccess = {},
            onAutoLoginFail = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}