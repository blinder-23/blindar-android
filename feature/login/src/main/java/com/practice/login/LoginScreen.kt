package com.practice.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.theme.BlindarTheme
import com.practice.designsystem.theme.NanumSquareRound

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (text, loginButton) = createRefs()
        Text(
            text = "로그인 화면",
            modifier = Modifier.constrainAs(text) {
                linkTo(parent.start, parent.top, parent.end, parent.bottom)
            },
            fontFamily = NanumSquareRound,
        )
        Button(
            onClick = onLoginSuccess,
            modifier = Modifier.constrainAs(loginButton) {
                linkTo(parent.start, text.top, parent.end, parent.bottom, topMargin = 100.dp)
            }
        ) {
            Text(
                text = "로그인",
                fontFamily = NanumSquareRound,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun LoginScreenPreview() {
    BlindarTheme {
        LoginScreen(
            onLoginSuccess = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}