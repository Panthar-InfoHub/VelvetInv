package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.sharad.velvetinvestment.utils.WindowSize


@Composable
fun BaseNavigation(windowSize: WindowSize) {

    val navController=rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.MainAppFlow
    ){
        composable<Route.LoginFlow>{
            LoginNavigation(
                windowSize=windowSize,
                onLoginSuccessNavigation = {}
            )
        }

        composable<Route.MainAppFlow> {
            AppNavigation()
        }
    }

}