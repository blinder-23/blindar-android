package com.practice.onboarding.onboarding

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.TitleLarge
import com.practice.designsystem.theme.BlindarTheme
import com.practice.designsystem.theme.NanumSquareRound
import com.practice.onboarding.R
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onPhoneLogin: () -> Unit,
    onGoogleLogin: suspend (Task<GoogleSignInAccount>) -> Unit,
    googleSignInClient: GoogleSignInClient,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val startForResult =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(it)
                    coroutineScope.launch { onGoogleLogin(task) }
                }
            }
        }
    val buttonFraction = 0.8f
    ConstraintLayout(modifier = modifier) {
        val (text, phoneLoginButton, googleLoginButton) = createRefs()
        Text(
            text = "Onboarding!",
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            fontFamily = NanumSquareRound,
        )
        PhoneLoginButton(
            onPhoneLogin = onPhoneLogin,
            modifier = Modifier
                .constrainAs(phoneLoginButton) {
                    top.linkTo(text.bottom, margin = 100.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(fraction = buttonFraction),
        )
        GoogleLoginButton(
            onGoogleLogin = {
                startForResult.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier
                .constrainAs(googleLoginButton) {
                    start.linkTo(parent.start)
                    top.linkTo(phoneLoginButton.bottom, margin = 50.dp)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(fraction = buttonFraction),
        )
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
                textColor = MaterialTheme.colorScheme.onPrimary,
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
@Composable
private fun OnboardingScreenPreview() {
    BlindarTheme {
        val context = LocalContext.current
        OnboardingScreen(
            onPhoneLogin = {},
            onGoogleLogin = {},
            googleSignInClient = GoogleSignIn.getClient(
                context,
                GoogleSignInOptions.Builder().build()
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}