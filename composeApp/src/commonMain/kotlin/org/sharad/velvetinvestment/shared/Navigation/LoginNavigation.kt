package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.features.login.ui.screens.LoginScreen
import org.sharad.velvetinvestment.features.login.ui.screens.OtpScreen
import org.sharad.velvetinvestment.features.login.ui.viewmodels.LoginScreenViewModel
import org.sharad.velvetinvestment.features.splash.ui.compose.SplashScreen
import org.sharad.velvetinvestment.utils.WindowSize

@Composable
fun LoginNavigation(
    windowSize: WindowSize,
    navigateToOnboardingGraph: () -> Unit,
    navigateToMainAppFlow: () -> Unit
){

    val navController= rememberNavController()
    val loginViewModel: LoginScreenViewModel = koinViewModel()



    NavHost(
        navController = navController,
        startDestination = Route.SplashScreen,
        // Forward navigation animation
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it }, // From Right
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        },

        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it }, // To Left
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        },

        // Back navigation animation
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it }, // From Left
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        },

        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it }, // To Right
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        }
    ){
        composable<Route.SplashScreen> {
            SplashScreen(
                windowSize=windowSize,
                onGetStarted={
                    navController.navigate(Route.LoginScreen)
                }
            )
        }
        composable<Route.LoginScreen> {
            LoginScreen(
                windowSize=windowSize,
                onOtpSend={
                    navController.navigate(Route.OtpScreen)
                },
                viewModel=loginViewModel
            )
        }
        composable<Route.OtpScreen> {
            OtpScreen(
                viewModel = loginViewModel,
                navigateToMainApp = navigateToMainAppFlow,
                navigateToOnboarding = navigateToOnboardingGraph
            )
        }
    }

}