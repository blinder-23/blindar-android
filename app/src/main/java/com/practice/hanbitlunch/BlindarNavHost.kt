package com.practice.hanbitlunch

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.practice.hanbitlunch.BlindarRoute.Companion.toBlindarRoute
import com.practice.main.main.MainScreen
import com.practice.onboarding.onboarding.OnboardingScreen
import com.practice.onboarding.splash.SplashScreen
import com.practice.register.phonenumber.VerifyPhoneNumber
import com.practice.register.registerform.RegisterFormScreen
import com.practice.register.selectschool.SelectSchoolScreen
import com.practice.settings.Settings
import com.practice.util.makeToast

private const val TAG = "BlindarNavHost"

@Composable
fun BlindarNavHost(
    windowSizeClass: WindowSizeClass,
    googleSignInClient: GoogleSignInClient,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onNavigateToMainScreen: () -> Unit = {},
) {
    val tweenSpec = tween<IntOffset>(
        durationMillis = 250,
    )

    NavHost(
        navController = navController,
        startDestination = BlindarRoute.Splash,
        modifier = modifier,
        enterTransition = {
            if ((initialState.toBlindarRoute() is BlindarRoute.Splash && targetState.toBlindarRoute() is BlindarRoute.Onboarding)) {
                fadeIn(initialAlpha = 1f)
            } else {
                val direction = animationDirection(initialState, targetState)
                slideIntoContainer(
                    towards = direction,
                    animationSpec = tweenSpec,
                )
            }
        },
        exitTransition = {
            if ((initialState.toBlindarRoute() is BlindarRoute.Splash && targetState.toBlindarRoute() !is BlindarRoute.Main) ||
                (initialState.toBlindarRoute() !is BlindarRoute.SelectSchool && targetState.toBlindarRoute() is BlindarRoute.Main)
            ) {
                fadeOut()
            } else {
                val direction = animationDirection(initialState, targetState)
                slideOutOfContainer(
                    towards = direction,
                    animationSpec = tweenSpec,
                )
            }
        },
    ) {
        blindarMainNavGraph(
            navController = navController,
            windowSizeClass = windowSizeClass,
            googleSignInClient = googleSignInClient,
            onNavigateToMainScreen = onNavigateToMainScreen,
        )
    }
}

fun NavGraphBuilder.blindarMainNavGraph(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    googleSignInClient: GoogleSignInClient,
    onNavigateToMainScreen: () -> Unit,
) {
    val onLoginSuccess = {
        navController.navigate(BlindarRoute.Main) {
            popUpTo(BlindarRoute.Splash) { inclusive = true }
        }
    }
    val onUsernameNotSet = {
        navController.navigate(BlindarRoute.VerifyUsername) {
            popUpTo(BlindarRoute.Splash) { inclusive = true }
        }
    }
    val onSchoolNotSelected = {
        navController.navigate(BlindarRoute.SelectSchool) {
            popUpTo(BlindarRoute.Splash) { inclusive = true }
        }
    }
    val onAutoLoginFail = {
        navController.navigate(BlindarRoute.Onboarding) {
            popUpTo(BlindarRoute.Splash) { inclusive = true }
        }
    }
    val onBackButtonClick: () -> Unit = { navController.popBackStack() }
    composable<BlindarRoute.Splash> {
        SplashScreen(
            onAutoLoginSuccess = {
                Log.d(TAG, "auto login success")
                onNavigateToMainScreen()
                onLoginSuccess()
            },
            onUsernameNotSet = onUsernameNotSet,
            onSchoolNotSelected = onSchoolNotSelected,
            onAutoLoginFail = {
                Log.d(TAG, "auto login fail")
                onAutoLoginFail()
            },
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
    composable<BlindarRoute.Onboarding> {
        // TODO: Splash에서 올 때만 애니메이션 적용하도록 수정
        val context = LocalContext.current
        val failMessage = stringResource(id = R.string.google_login_fail_message)
        OnboardingScreen(
            onPhoneLogin = { navController.navigate(BlindarRoute.VerifyPhone) },
            onNewUserSignUp = { user ->
                Log.d(TAG, "new user with google: ${user.uid}")
                navController.navigate(BlindarRoute.SelectSchool)
            },
            onExistingUserLogin = { user ->
                Log.d(TAG, "existing user with google: ${user.uid}")
                navController.navigate(BlindarRoute.Main) {
                    popUpTo(BlindarRoute.Onboarding) { inclusive = true }
                }
            },
            onFail = {
                context.makeToast(failMessage)
            },
            googleSignInClient = googleSignInClient,
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
    composable<BlindarRoute.VerifyPhone> {
        VerifyPhoneNumber(
            onBackButtonClick = onBackButtonClick,
            onExistingUserLogin = {
                onNavigateToMainScreen()
                navController.navigate(BlindarRoute.Main) {
                    popUpTo(BlindarRoute.Onboarding) { inclusive = true }
                }
            },
            onNewUserSignUp = { navController.navigate(BlindarRoute.VerifyUsername) },
            onUsernameNotSet = onUsernameNotSet,
            onSchoolNotSelected = onSchoolNotSelected,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .safeDrawingPadding(),
        )
    }
    composable<BlindarRoute.VerifyUsername> {
        // RegisterForm과 SelectSchool은 같은 ViewModel을 공유
        RegisterFormScreen(
            onBackButtonClick = onBackButtonClick,
            onNameUpdated = {
                navController.navigate(BlindarRoute.SelectSchool)
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .safeDrawingPadding(),
        )
    }
    composable<BlindarRoute.SelectSchool> {
        SelectSchoolScreen(
            onBackButtonClick = onBackButtonClick,
            onNavigateToMain = {
                onNavigateToMainScreen()
                navController.navigate(BlindarRoute.Main) {
                    popUpTo(BlindarRoute.Onboarding) { inclusive = true }
                    popUpTo(BlindarRoute.Main) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .safeDrawingPadding(),
        )
    }
    composable<BlindarRoute.Main> {
        MainScreen(
            windowSize = windowSizeClass,
            viewModel = hiltViewModel(),
            modifier = Modifier.fillMaxSize(),
            onNavigateToSelectSchoolScreen = {
                navController.navigate(BlindarRoute.SelectSchool)
            },
            onNavigateToSettingsScreen = {
                navController.navigate(BlindarRoute.Settings)
            },
        )
    }
    composable<BlindarRoute.Settings> {
        Settings(
            onBackButtonClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize(),
        )
    }
}

fun NavGraphBuilder.registerGraph(
    navController: NavHostController,
    onUsernameNotSet: () -> Unit,
    onSchoolNotSelected: () -> Unit,
    onNavigateToMainScreen: () -> Unit,
) {

}

private fun animationDirection(initialState: NavBackStackEntry, targetState: NavBackStackEntry) =
    if (initialState.toBlindarRoute() > targetState.toBlindarRoute()) {
        AnimatedContentTransitionScope.SlideDirection.Right
    } else {
        AnimatedContentTransitionScope.SlideDirection.Left
    }