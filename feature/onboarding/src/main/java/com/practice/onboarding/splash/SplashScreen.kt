package com.practice.onboarding.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.AppIcon
import com.practice.designsystem.components.LabelMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.notification.BlindarNotificationManager
import com.practice.onboarding.R
import com.practice.user.UserRegisterState
import com.practice.work.dailyalarm.DailyNotificationAlarmReceiver
import kotlinx.coroutines.delay

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
        BlindarNotificationManager.createNotificationChannels(context)
        DailyNotificationAlarmReceiver.setInitialAlarm(context)
        delay(300L) // SupportText를 충분히 오래 보여주기 위함
        when (viewModel.getUserRegisterState()) {
            UserRegisterState.NOT_LOGGED_IN -> onAutoLoginFail()
            UserRegisterState.USERNAME_MISSING -> onUsernameNotSet()
            UserRegisterState.SCHOOL_NOT_SELECTED -> onSchoolNotSelected()
            UserRegisterState.ALL_FILLED -> onAutoLoginSuccess()
            UserRegisterState.AUTO_LOGIN -> {
                viewModel.uploadUserInfoOnAutoLogin(context)
                onAutoLoginSuccess()
            }
        }
    }

    SplashScreen(
        modifier = modifier,
    )
}

@Composable
private fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (icon, supportText) = createRefs()
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
        SupportText(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 48.dp)
                .constrainAs(supportText) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
private fun SupportText(modifier: Modifier = Modifier) {
    LabelMedium(
        text = stringResource(id = R.string.received_support_text),
        modifier = modifier,
        textAlign = TextAlign.Center,
        textColor = MaterialTheme.colorScheme.onSurface,
    )
}

@LightAndDarkPreview
@Composable
private fun SplashScreenPreview() {
    BlindarTheme {
        SplashScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}