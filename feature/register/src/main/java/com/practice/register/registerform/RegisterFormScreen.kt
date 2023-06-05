package com.practice.register.registerform

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.theme.BlindarTheme
import com.practice.designsystem.theme.NanumSquareRound
import com.practice.register.R

@Composable
fun RegisterFormScreen(
    onLaunchRegisterUI: () -> Unit,
    onBackButtonClick: () -> Unit,
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val signInLauncher =
        rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) { result ->
            onSignInResult(result)
        }
    ConstraintLayout(modifier = modifier) {
        val (appBar, text, registerButton, nextButton) = createRefs()
        BlindarTopAppBar(
            title = stringResource(id = R.string.register_form_screen),
            onBackButtonClick = onBackButtonClick,
            modifier = Modifier.constrainAs(appBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        Text(
            text = "회원가입 폼",
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            fontFamily = NanumSquareRound,
        )
        Button(
            onClick = { },
            modifier = Modifier.constrainAs(registerButton) {
                top.linkTo(text.bottom, margin = 50.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        ) {
            Text(text = "회원가입 UI")
        }
        Button(
            onClick = onNextButtonClick,
            modifier = Modifier.constrainAs(nextButton) {
                top.linkTo(registerButton.bottom, margin = 100.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(
                text = "다음",
                fontFamily = NanumSquareRound,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun RegisterFormScreenPreview() {
    BlindarTheme {
        RegisterFormScreen(
            onLaunchRegisterUI = {},
            onBackButtonClick = {},
            onNextButtonClick = { },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
    val response = result.idpResponse
    when (result.resultCode) {
        ComponentActivity.RESULT_OK -> {
            Log.d("Firebase Login", "Success: ${FirebaseAuth.getInstance().currentUser}")
        }

        else -> {
            Log.d("Firebase Login", "Fail: $response")
        }
    }
}