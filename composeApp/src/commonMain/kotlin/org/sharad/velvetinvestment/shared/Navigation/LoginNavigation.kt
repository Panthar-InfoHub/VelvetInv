package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.sharad.velvetinvestment.presentation.LoginScreen.LoginScreen
import org.sharad.velvetinvestment.presentation.SplashScreen.SplashScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnboardingScreenRoot
import org.sharad.velvetinvestment.utils.WindowSize

@Composable
fun LoginNavigation(
    onLoginSuccessNavigation: () -> Unit,
    windowSize: WindowSize
){

    val navController= rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.OnBoardingFlow(1),
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
                onLoginSuccessNavigation=onLoginSuccessNavigation
            )
        }
        composable<Route.OnBoardingFlow> {
            val index=it.toRoute<Route.OnBoardingFlow>().index
            OnboardingScreenRoot(index)
        }
    }

}