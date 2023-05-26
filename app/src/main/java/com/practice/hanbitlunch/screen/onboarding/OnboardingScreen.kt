package com.practice.hanbitlunch.screen.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.practice.hanbitlunch.screen.LightAndDarkPreview
import com.practice.hanbitlunch.theme.BlindarTheme

@Composable
fun OnboardingScreen(
    onRegister: () -> Unit,
    onGoogleLogin: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (text, registerButton, loginButton) = createRefs()
        Text(
            text = "Onboarding!",
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        Button(
            onClick = onRegister,
            modifier = Modifier.constrainAs(registerButton) {
                top.linkTo(text.bottom, margin = 100.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(
                text = "가입하기"
            )
        }
        Button(
            onClick = onLogin,
            modifier = Modifier.constrainAs(loginButton) {
                top.linkTo(registerButton.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(text = "로그인하기")
        }
    }
}

@LightAndDarkPreview
@Composable
private fun OnboardingScreenPreview() {
    BlindarTheme {
        OnboardingScreen(
            onRegister = {},
            onGoogleLogin = {},
            onLogin = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}