package com.practice.hanbitlunch.screen.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.practice.hanbitlunch.screen.MainScreen
import com.practice.hanbitlunch.screen.MainScreenViewModel
import com.practice.hanbitlunch.screen.login.LoginScreen
import com.practice.hanbitlunch.screen.onboarding.OnboardingScreen
import com.practice.hanbitlunch.screen.registerform.RegisterFormScreen
import com.practice.hanbitlunch.screen.selectschool.SelectSchoolScreen
import com.practice.hanbitlunch.screen.splash.SplashScreen
import kotlinx.coroutines.delay

private const val SPLASH = "splash"
private const val ONBOARDING = "onboarding"
private const val REGISTER_FORM = "register_form"
private const val SELECT_SCHOOL = "select_school"
private const val LOGIN = "login"
private const val MAIN = "main_screen"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BlindarNavHost(
    windowSizeClass: WindowSizeClass,
    onMainScreenLaunch: suspend () -> Unit,
    onMainScreenRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val mainScreenViewModel: MainScreenViewModel = hiltViewModel()
    AnimatedNavHost(
        navController = navController,
        startDestination = SPLASH,
        modifier = modifier,
        enterTransition = {
            // TODO: destination에 따라 fadeIn과 slideIn 구별해서 적용
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = 200,
                ),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = 200,
                ),
            )
        },
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
                onRegister = { navController.navigate(REGISTER_FORM) },
                onGoogleLogin = { /*TODO*/ },
                onLogin = { navController.navigate(LOGIN) },
                modifier = Modifier.fillMaxSize(),
            )
        }
        composable(REGISTER_FORM) {
            // RegisterForm과 SelectSchool은 같은 ViewModel을 공유
            RegisterFormScreen(
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
                viewModel = mainScreenViewModel,
                modifier = Modifier.fillMaxSize(),
                onLaunch = onMainScreenLaunch,
                onRefresh = onMainScreenRefresh,
            )
        }
    }
}