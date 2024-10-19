package com.practice.hanbitlunch

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.feedback.FeedbackRoute
import com.practice.feedback.FeedbackScreen
import com.practice.hanbitlunch.navigation.memoRoute
import com.practice.hanbitlunch.navigation.nutrientRoute
import com.practice.main.main.MainScreen
import com.practice.main.memo.MemoRoute
import com.practice.main.memo.MemoScreen
import com.practice.main.nutrient.NutrientRoute
import com.practice.main.nutrient.NutrientScreen
import com.practice.onboarding.OnboardingRoute
import com.practice.onboarding.OnboardingScreen
import com.practice.selectschool.compose.SelectSchoolScreen
import com.practice.settings.settings.Settings
import com.practice.splash.SplashScreen
import com.practice.username.RegisterFormScreen
import com.practice.util.makeToast
import com.practice.verifyphone.VerifyPhoneNumber

private const val TAG = "BlindarNavHost"

@Composable
fun BlindarNavHost(
    windowSizeClass: WindowSizeClass,
    signInWithGoogleRequest: GetCredentialRequest,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onNavigateToMainScreen: () -> Unit = {},
) {
    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colorScheme.surface
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(systemBarColor)
        systemUiController.setNavigationBarColor(systemBarColor)
    }

    val tweenSpec = tween<IntOffset>(
        durationMillis = 500,
    )

    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier,
        enterTransition = {
            if (initialState.toBlindarRoute() is SplashRoute && targetState.toBlindarRoute() is OnboardingRoute) {
                fadeIn(initialAlpha = 1f)
            } else if (targetState.toBlindarRoute() is OnboardingRoute) {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tweenSpec,
                )
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
            } else if (initialState.toBlindarRoute() !is SplashRoute && targetState.toBlindarRoute() is OnboardingRoute) {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tweenSpec,
                )
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
            onNavigateToMainScreen = onNavigateToMainScreen,
            credentialManager = CredentialManager.create(context),
            signInWithGoogleRequest = signInWithGoogleRequest,
        )
    }
}

fun NavGraphBuilder.blindarMainNavGraph(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    onNavigateToMainScreen: () -> Unit,
    credentialManager: CredentialManager,
    signInWithGoogleRequest: GetCredentialRequest,
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
        navController.navigate(OnboardingRoute(playAnimation = true)) {
            popUpTo(SplashRoute) { inclusive = true }
        }
    }
    val onBackButtonClick: () -> Unit = { navController.popBackStack() }
    composable<SplashRoute> {
        SplashScreen(
            onAutoLoginSuccess = {
                onNavigateToMainScreen()
                onLoginSuccess()
            },
            onUsernameNotSet = onUsernameNotSet,
            onSchoolNotSelected = onSchoolNotSelected,
            onAutoLoginFail = {
                onAutoLoginFail()
            },
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
    composable<OnboardingRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<OnboardingRoute>()
        val context = LocalContext.current
        OnboardingScreen(
            route = route,
            onPhoneLogin = { navController.navigate(VerifyPhoneRoute) },
            onNewUserSignUp = { user ->
                navController.navigate(SelectSchoolRoute)
            },
            onExistingUserLogin = { user ->
                navController.navigate(MainRoute) {
                    popUpTo<OnboardingRoute> { inclusive = true }
                }
            },
            onFail = {
                context.makeToast(R.string.google_login_fail_message)
            },
            credentialManager = credentialManager,
            signInWithGoogleRequest = signInWithGoogleRequest,
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
    composable<VerifyPhoneRoute> {
        VerifyPhoneNumber(
            onBackButtonClick = {
                navController.navigate(OnboardingRoute()) {
                    popUpTo<OnboardingRoute> { inclusive = true }
                }
            },
            onExistingUserLogin = {
                onNavigateToMainScreen()
                navController.navigate(MainRoute) {
                    popUpTo<OnboardingRoute> { inclusive = true }
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
            onNavigateToNext = {
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
                    popUpTo<OnboardingRoute> { inclusive = true }
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
            onNavigateToMemoScreen = { mainUiState ->
                navController.navigate(mainUiState.memoRoute)
            },
        )
    }
    composable<SettingsRoute> {
        Settings(
            onBackButtonClick = {
                navController.popBackStack()
            },
            onLogout = {
                navController.navigate(OnboardingRoute(playAnimation = false)) {
                    popUpTo(MainRoute) { inclusive = true }
                }
            },
            onFeedbackButtonClick = {
                navController.navigate(FeedbackRoute)
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
    composable<MemoRoute> {
        MemoScreen(
            onNavigateToBack = {
                navController.popBackStack()
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .safeDrawingPadding()
                .fillMaxSize(),
        )
    }
    composable<FeedbackRoute> {
        FeedbackScreen(
            onNavigateBack = { navController.popBackStack() },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .safeDrawingPadding()
                .fillMaxSize(),
        )
    }
}