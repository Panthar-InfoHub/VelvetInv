package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.sharad.velvetinvestment.presentation.LoginScreen.LoginScreen
import org.sharad.velvetinvestment.presentation.SplashScreen.SplashScreen
import org.sharad.velvetinvestment.utils.WindowSize

@Composable
fun LoginNavigation(
    onLoginSuccessNavigation: () -> Unit,
    windowSize: WindowSize
){

    val navController= rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SplashScreen
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
            LoginScreen(windowSize=windowSize)
        }
    }

}