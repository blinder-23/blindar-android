package com.practice.onboarding.onboarding

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.practice.designsystem.LightPreview
import com.practice.designsystem.LightTabletPreview
import com.practice.designsystem.components.AppIcon
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.onboarding.R
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onPhoneLogin: () -> Unit,
    onNewUserSignUp: (FirebaseUser) -> Unit,
    onExistingUserLogin: (FirebaseUser) -> Unit,
    onFail: () -> Unit,
    googleSignInClient: GoogleSignInClient,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val startForResult = rememberGoogleLoginRequestLauncher(
        onGoogleLogin = { intent ->
            coroutineScope.launch {
                viewModel.tryGoogleLogin(
                    context = context,
                    intent = intent,
                    onNewUserSignUp = onNewUserSignUp,
                    onExistingUserLogin = onExistingUserLogin,
                    onFail = onFail,
                )
            }
        },
        onFail = viewModel::onGoogleLoginLauncherFail,
    )

    val appIconOffset = remember { Animatable(0f) }
    val buttonAlpha = remember { Animatable(0f) }
    LaunchedEffect(true) {
        appIconOffset.animateTo(
            targetValue = -125f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = CubicBezierEasing(0.5f, 0.0f, 0.5f, 1f),
            )
        )
        buttonAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
            ),
        )
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
                    startForResult.launch(googleSignInClient.signInIntent)
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
private fun rememberGoogleLoginRequestLauncher(
    onGoogleLogin: (Intent) -> Unit,
    onFail: (ActivityResult) -> Unit,
) =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let(onGoogleLogin)
        } else {
            onFail(result)
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
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
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
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
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
            textColor = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@LightPreview
@LightTabletPreview
@Composable
private fun OnboardingScreenPreview() {
    BlindarTheme {
        val context = LocalContext.current
        val client = GoogleSignIn.getClient(context, GoogleSignInOptions.Builder().build())
        OnboardingScreen(
            onPhoneLogin = {},
            onNewUserSignUp = {},
            onExistingUserLogin = {},
            onFail = {},
            googleSignInClient = client,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}