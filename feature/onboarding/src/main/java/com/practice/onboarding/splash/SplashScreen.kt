package com.practice.onboarding.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.AppIcon
import com.practice.designsystem.theme.BlindarTheme
import com.practice.user.UserRegisterState

@Composable
fun SplashScreen(
    onAutoLoginSuccess: () -> Unit,
    onUsernameNotSet: () -> Unit,
    onSchoolNotSelected: () -> Unit,
    onAutoLoginFail: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colorScheme.surface
    val context = LocalContext.current
    LaunchedEffect(true) {
        systemUiController.setSystemBarsColor(systemBarColor)
        when (viewModel.getUserRegisterState()) {
            UserRegisterState.NOT_LOGGED_IN -> onAutoLoginFail()
            UserRegisterState.USERNAME_MISSING -> onUsernameNotSet()
            UserRegisterState.SCHOOL_NOT_SELECTED -> onSchoolNotSelected()
            UserRegisterState.ALL_FILLED -> {
                onAutoLoginSuccess()
                viewModel.uploadUserInfoToFirebaseOnAutoLogin(context)
            }
        }
        viewModel.enqueueOneTimeWorkIfFirstExecution(context)
    }

    ConstraintLayout(modifier = modifier) {
        val (icon) = createRefs()
        AppIcon(
            modifier = Modifier.constrainAs(icon) {
                linkTo(
                    start = parent.start,
                    top = parent.top,
                    end = parent.end,
                    bottom = parent.bottom,
                )
            }
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SplashScreenPreview() {
    BlindarTheme {
        SplashScreen(
            onAutoLoginSuccess = {},
            onUsernameNotSet = {},
            onSchoolNotSelected = {},
            onAutoLoginFail = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}