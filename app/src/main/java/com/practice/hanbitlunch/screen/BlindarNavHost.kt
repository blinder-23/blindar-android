package com.practice.hanbitlunch.screen

import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.practice.hanbitlunch.R
import com.practice.main.MainScreen
import com.practice.onboarding.onboarding.OnboardingScreen
import com.practice.onboarding.splash.SplashScreen
import com.practice.register.phonenumber.VerifyPhoneNumber
import com.practice.register.registerform.RegisterFormScreen
import com.practice.register.selectschool.SelectSchoolScreen
import com.practice.util.makeToast

private val TAG = "BlindarNavHost"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BlindarNavHost(
    windowSizeClass: WindowSizeClass,
    googleSignInClient: GoogleSignInClient,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val tweenSpec = tween<IntOffset>(
        durationMillis = 200,
    )
    AnimatedNavHost(
        navController = navController,
        startDestination = SPLASH,
        modifier = modifier,
        enterTransition = {
            if ((initialState.route == SPLASH && targetState.route == ONBOARDING) ||
                targetState.route == MAIN ||
                (initialState.route == MAIN && targetState.route == SELECT_SCHOOL)
            ) {
                fadeIn()
            } else {
                val direction = animationDirection(initialState, targetState)
                slideIntoContainer(
                    towards = direction,
                    animationSpec = tweenSpec,
                )
            }
        },
        exitTransition = {
            if (initialState.route == SPLASH || targetState.route == MAIN ||
                (initialState.route == MAIN && targetState.route == SELECT_SCHOOL)
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
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.blindarMainNavGraph(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    googleSignInClient: GoogleSignInClient,
) {
    composable(SPLASH) {
        SplashScreen(
            login = {
                val user = FirebaseAuth.getInstance().currentUser
                Log.d(TAG, "current user is ${user?.uid}")
                user != null
            },
            onAutoLoginSuccess = {
                Log.d(TAG, "auto login success")
                navController.navigate(MAIN) {
                    popUpTo(SPLASH) { inclusive = true }
                }
            },
            onAutoLoginFail = {
                Log.d(TAG, "auto login fail")
                navController.navigate(ONBOARDING) {
                    popUpTo(SPLASH) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
    composable(ONBOARDING) {
        val context = LocalContext.current
        val failMessage = stringResource(id = R.string.google_login_fail_message)
        OnboardingScreen(
            onPhoneLogin = { navController.navigate(REGISTER) },
            onNewUserSignUp = { user ->
                Log.d(TAG, "new user with google: ${user.uid}")
                navController.navigate(SELECT_SCHOOL)
            },
            onExistingUserLogin = { user ->
                Log.d(TAG, "existing user with google: ${user.uid}")
                navController.navigate(MAIN) {
                    popUpTo(ONBOARDING) { inclusive = true }
                }
            },
            onFail = {
                context.makeToast(failMessage)
            },
            googleSignInClient = googleSignInClient,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
    registerGraph(navController)
    composable(MAIN) {
        MainScreen(
            windowSize = windowSizeClass,
            viewModel = hiltViewModel(),
            modifier = Modifier.fillMaxSize(),
            onNavigateToSelectSchoolScreen = {
                navController.navigate(SELECT_SCHOOL)
            },
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.registerGraph(navController: NavHostController) {
    val onBackButtonClick: () -> Unit = { navController.popBackStack() }
    navigation(startDestination = VERIFY_PHONE, route = REGISTER) {
        composable(VERIFY_PHONE) {
            VerifyPhoneNumber(
                onBackButtonClick = onBackButtonClick,
                onExistingUserLogin = {
                    navController.navigate(MAIN) {
                        popUpTo(ONBOARDING) { inclusive = true }
                    }
                },
                onNewUserSignUp = { navController.navigate(REGISTER_FORM) },
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .safeDrawingPadding(),
            )
        }
        composable(REGISTER_FORM) {
            // RegisterForm과 SelectSchool은 같은 ViewModel을 공유
            RegisterFormScreen(
                onBackButtonClick = onBackButtonClick,
                onNameUpdated = {
                    navController.navigate(SELECT_SCHOOL)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .safeDrawingPadding(),
            )
        }
        composable(SELECT_SCHOOL) {
            SelectSchoolScreen(
                onBackButtonClick = onBackButtonClick,
                onNavigateToMain = {
                    navController.navigate(MAIN) {
                        popUpTo(ONBOARDING) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .safeDrawingPadding(),
            )
        }
    }
}

private const val SPLASH = "splash"
private const val ONBOARDING = "onboarding"
private const val REGISTER = "register"
private const val VERIFY_PHONE = "verify_phone"
private const val REGISTER_FORM = "register_form"
private const val SELECT_SCHOOL = "select_school"
private const val MAIN = "main_screen"

private val NavBackStackEntry.route: String?
    get() = this.destination.route

private fun priority(state: NavBackStackEntry) = when (state.route) {
    SPLASH -> 0
    ONBOARDING -> 1
    VERIFY_PHONE -> 2
    REGISTER_FORM -> 3
    SELECT_SCHOOL -> 4
    MAIN -> 6
    else -> 7
}

@OptIn(ExperimentalAnimationApi::class)
private fun animationDirection(initialState: NavBackStackEntry, targetState: NavBackStackEntry) =
    if (priority(initialState) > priority(targetState)) {
        AnimatedContentScope.SlideDirection.Right
    } else {
        AnimatedContentScope.SlideDirection.Left
    }