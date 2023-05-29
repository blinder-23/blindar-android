package com.practice.hanbitlunch.screen.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.practice.hanbitlunch.screen.LightAndDarkPreview
import com.practice.hanbitlunch.theme.BlindarTheme
import com.practice.hanbitlunch.theme.NanumSquareRound

@Composable
fun SplashScreen(
    login: suspend () -> Boolean,
    onLoginSuccess: () -> Unit,
    onLoginFail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(true) {
        if (login()) {
            onLoginSuccess()
        } else {
            onLoginFail()
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
            onLoginSuccess = {},
            onLoginFail = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}