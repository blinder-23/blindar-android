package com.practice.hanbitlunch.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.practice.login.LoginScreen
import com.practice.main.MainScreen
import com.practice.onboarding.onboarding.OnboardingScreen
import com.practice.onboarding.splash.SplashScreen
import com.practice.register.phonenumber.VerifyPhoneNumber
import com.practice.register.registerform.RegisterFormScreen
import com.practice.register.selectschool.SelectSchoolScreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BlindarNavHost(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val mainScreenViewModel: com.practice.main.MainScreenViewModel = hiltViewModel()
    val tweenSpec = tween<IntOffset>(
        durationMillis = 200,
    )
    AnimatedNavHost(
        navController = navController,
        startDestination = SPLASH,
        modifier = modifier,
        enterTransition = {
            if ((initialState.route == SPLASH && targetState.route == ONBOARDING) ||
                targetState.route == MAIN
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
            if (initialState.route == SPLASH || targetState.route == MAIN) {
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
        blindarMainNavGraph(navController, windowSizeClass)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.blindarMainNavGraph(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass
) {
    composable(SPLASH) {
        SplashScreen(
            login = {
                // auto login code
                delay(1000L)
                false
            },
            onLoginSuccess = {
                navController.navigate(MAIN) {
                    popUpTo(SPLASH) { inclusive = true }
                }
            },
            onLoginFail = {
                navController.navigate(ONBOARDING) {
                    popUpTo(SPLASH) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
    composable(ONBOARDING) {
        OnboardingScreen(
            onRegister = { navController.navigate(REGISTER) },
            onGoogleLogin = { /*TODO*/ },
            onLogin = { navController.navigate(LOGIN) },
            modifier = Modifier.fillMaxSize(),
        )
    }
    registerGraph(navController)
    composable(LOGIN) {
        LoginScreen(
            onLoginSuccess = {
                navController.navigate(MAIN) {
                    popUpTo(ONBOARDING) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
    composable(MAIN) {
        MainScreen(
            windowSize = windowSizeClass,
            viewModel = hiltViewModel(),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.registerGraph(navController: NavHostController) {
    navigation(startDestination = VERIFY_PHONE, route = REGISTER) {
        composable(VERIFY_PHONE) {
            VerifyPhoneNumber(
                onNextButtonClick = {
                    navController.navigate(REGISTER_FORM) {

                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
        composable(REGISTER_FORM) {
            // RegisterForm과 SelectSchool은 같은 ViewModel을 공유
            RegisterFormScreen(
                onLaunchRegisterUI = {},
                onNextButtonClick = {
                    navController.navigate(SELECT_SCHOOL)
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
        composable(SELECT_SCHOOL) {
            SelectSchoolScreen(
                onNavigateToMain = {
                    navController.navigate(MAIN) {
                        popUpTo(ONBOARDING) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxSize(),
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
private const val LOGIN = "login"
private const val MAIN = "main_screen"

private val NavBackStackEntry.route: String?
    get() = this.destination.route

private fun priority(state: NavBackStackEntry) = when (state.route) {
    SPLASH -> 0
    ONBOARDING -> 1
    VERIFY_PHONE -> 2
    REGISTER_FORM -> 3
    SELECT_SCHOOL -> 4
    LOGIN -> 5
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