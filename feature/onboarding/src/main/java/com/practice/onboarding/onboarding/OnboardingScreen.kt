package com.practice.onboarding.onboarding

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseUser
import com.practice.designsystem.LightPreview
import com.practice.designsystem.LightTabletPreview
import com.practice.designsystem.components.AppIcon
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.onboarding.R
import com.practice.onboarding.onboarding.animation.animateAppIconOffset
import com.practice.onboarding.onboarding.animation.animateButtonAlpha
import com.practice.onboarding.onboarding.animation.getAppIconOffset
import com.practice.onboarding.onboarding.animation.getButtonAlpha
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    route: OnboardingRoute,
    onPhoneLogin: () -> Unit,
    onNewUserSignUp: (FirebaseUser) -> Unit,
    onExistingUserLogin: (FirebaseUser) -> Unit,
    onFail: () -> Unit,
    credentialManager: CredentialManager,
    signInWithGoogleRequest: GetCredentialRequest,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val appIconOffset = getAppIconOffset(playAnimation = route.playAnimation)
    val buttonAlpha = getButtonAlpha(playAnimation = route.playAnimation)
    LaunchedEffect(true) {
        if (route.playAnimation) {
            appIconOffset.animateAppIconOffset()
            buttonAlpha.animateButtonAlpha()
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val buttonWidthRatio = if (minWidth < minHeight) 0.8f else 0.5f
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val buttonGuideline = createGuidelineFromBottom(0.05f)
            val (icon, phoneLoginButton, googleLoginButton) = createRefs()
            AppIcon(
                modifier = Modifier.constrainAs(icon) {
                    linkTo(
                        start = parent.start,
                        top = parent.top,
                        end = parent.end,
                        bottom = parent.bottom,
                    )
                    translationY = appIconOffset.value.dp
                }
            )
            GoogleLoginButton(
                onGoogleLogin = {
                    coroutineScope.launch {
                        val result = credentialManager.getCredential(
                            context = context.applicationContext,
                            request = signInWithGoogleRequest,
                        )
                        Log.d("OnboardingScreen", "${result.credential.type}")
                        viewModel.parseIdToken(
                            result = result,
                            onNewUserSignUp = onNewUserSignUp,
                            onExistingUserLogin = onExistingUserLogin,
                            onFail = onFail,
                        )
                    }
                },
                modifier = Modifier
                    .constrainAs(googleLoginButton) {
                        start.linkTo(parent.start)
                        bottom.linkTo(phoneLoginButton.top, margin = 20.dp)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(fraction = buttonWidthRatio)
                    .alpha(buttonAlpha.value),
            )
            PhoneLoginButton(
                onPhoneLogin = onPhoneLogin,
                modifier = Modifier
                    .constrainAs(phoneLoginButton) {
                        start.linkTo(parent.start)
                        bottom.linkTo(buttonGuideline)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(fraction = buttonWidthRatio)
                    .alpha(buttonAlpha.value),
            )
        }
    }
}

@Composable
private fun PhoneLoginButton(
    onPhoneLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val phoneLoginText = stringResource(id = R.string.sign_in_with_phone)
    TextButton(
        onClick = onPhoneLogin,
        modifier = modifier.clearAndSetSemantics {
            contentDescription = phoneLoginText
            role = Role.Button
        },
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Box(
            modifier = Modifier.heightIn(min = 46.dp),
            contentAlignment = Alignment.Center,
        ) {
            TitleLarge(
                text = phoneLoginText,
            )
        }
    }
}

@Composable
private fun GoogleLoginButton(
    onGoogleLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val loginText = stringResource(id = R.string.sign_in_with_google)
    ElevatedButton(
        onClick = onGoogleLogin,
        modifier = modifier.clearAndSetSemantics {
            contentDescription = loginText
            role = Role.Button
        },
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.google_light),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
            tint = Color.Unspecified,
        )
        TitleLarge(
            text = loginText,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@LightPreview
@LightTabletPreview
@Composable
private fun OnboardingScreenPreview() {
    BlindarTheme {
        val context = LocalContext.current
        OnboardingScreen(
            route = OnboardingRoute(playAnimation = true),
            onPhoneLogin = {},
            onNewUserSignUp = {},
            onExistingUserLogin = {},
            onFail = {},
            credentialManager = CredentialManager.create(context),
            signInWithGoogleRequest = GetCredentialRequest.Builder().build(),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}