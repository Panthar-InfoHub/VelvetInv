package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.sharad.velvetinvestment.utils.WindowSize


@Composable
fun BaseNavigation(windowSize: WindowSize, pv: PaddingValues) {

    val navController=rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.MainAppFlow,
        modifier = Modifier.padding(top=pv.calculateTopPadding())
    ){
        composable<Route.LoginFlow>{
            LoginNavigation(
                windowSize=windowSize,
                onLoginSuccessNavigation = {navController.navigate(Route.MainAppFlow)}
            )
        }

        composable<Route.MainAppFlow> {
            AppNavigation()
        }
    }

}