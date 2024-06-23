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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.practice.hanbitlunch.navigation.nutrientRoute
import com.practice.main.main.MainScreen
import com.practice.main.nutrient.NutrientRoute
import com.practice.main.nutrient.NutrientScreen
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
        durationMillis = 500,
    )

    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier,
        enterTransition = {
            if (targetState.toBlindarRoute() is OnboardingRoute) {
                fadeIn(initialAlpha = 1f)
            } else {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tweenSpec,
                )
            }
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tweenSpec,
            )
        },
        exitTransition = {
            if ((initialState.toBlindarRoute() is SplashRoute && targetState.toBlindarRoute() !is MainRoute)) {
                fadeOut(targetAlpha = 1f)
            } else {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tweenSpec,
                )
            }
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tweenSpec,
            )
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
        navController.navigate(MainRoute) {
            popUpTo(SplashRoute) { inclusive = true }
        }
    }
    val onUsernameNotSet = {
        navController.navigate(VerifyUsernameRoute) {
            popUpTo(SplashRoute) { inclusive = true }
        }
    }
    val onSchoolNotSelected = {
        navController.navigate(SelectSchoolRoute) {
            popUpTo(SplashRoute) { inclusive = true }
        }
    }
    val onAutoLoginFail = {
        navController.navigate(OnboardingRoute) {
            popUpTo(SplashRoute) { inclusive = true }
        }
    }
    val onBackButtonClick: () -> Unit = { navController.popBackStack() }
    composable<SplashRoute> {
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
    composable<OnboardingRoute> {
        // TODO: Splash에서 올 때만 애니메이션 적용하도록 수정
        val context = LocalContext.current
        val failMessage = stringResource(id = R.string.google_login_fail_message)
        OnboardingScreen(
            onPhoneLogin = { navController.navigate(VerifyPhoneRoute) },
            onNewUserSignUp = { user ->
                Log.d(TAG, "new user with google: ${user.uid}")
                navController.navigate(SelectSchoolRoute)
            },
            onExistingUserLogin = { user ->
                Log.d(TAG, "existing user with google: ${user.uid}")
                navController.navigate(MainRoute) {
                    popUpTo(OnboardingRoute) { inclusive = true }
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
    composable<VerifyPhoneRoute> {
        VerifyPhoneNumber(
            onBackButtonClick = onBackButtonClick,
            onExistingUserLogin = {
                onNavigateToMainScreen()
                navController.navigate(MainRoute) {
                    popUpTo(OnboardingRoute) { inclusive = true }
                }
            },
            onNewUserSignUp = { navController.navigate(VerifyUsernameRoute) },
            onUsernameNotSet = onUsernameNotSet,
            onSchoolNotSelected = onSchoolNotSelected,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .safeDrawingPadding(),
        )
    }
    composable<VerifyUsernameRoute> {
        // RegisterForm과 SelectSchool은 같은 ViewModel을 공유
        RegisterFormScreen(
            onBackButtonClick = onBackButtonClick,
            onNameUpdated = {
                navController.navigate(SelectSchoolRoute)
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .safeDrawingPadding(),
        )
    }
    composable<SelectSchoolRoute> {
        SelectSchoolScreen(
            onBackButtonClick = onBackButtonClick,
            onNavigateToMain = {
                onNavigateToMainScreen()
                navController.navigate(MainRoute) {
                    popUpTo(OnboardingRoute) { inclusive = true }
                    popUpTo(MainRoute) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .safeDrawingPadding(),
        )
    }
    composable<MainRoute> {
        MainScreen(
            windowSize = windowSizeClass,
            viewModel = hiltViewModel(),
            modifier = Modifier.fillMaxSize(),
            onNavigateToSelectSchoolScreen = {
                navController.navigate(SelectSchoolRoute)
            },
            onNavigateToSettingsScreen = {
                navController.navigate(SettingsRoute)
            },
            onNavigateToNutrientScreen = { mainUiState ->
                navController.navigate(mainUiState.nutrientRoute)
            },
        )
    }
    composable<SettingsRoute> {
        Settings(
            onBackButtonClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize(),
        )
    }

    composable<NutrientRoute> {
        NutrientScreen(
            onNavigateToBack = {
                navController.popBackStack()
            },
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize(),
        )
    }
}