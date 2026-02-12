package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.homescreen.HomeScreenViewModel
import org.sharad.velvetinvestment.presentation.homescreen.compose.HomeScreenMain
import org.sharad.velvetinvestment.shared.BottomNavBar

@Composable
fun BottomNavigation() {

    val navController= rememberNavController()
    val homeViewModel: HomeScreenViewModel= koinViewModel()


    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color.White
    ) {
        val pv=it
        NavHost(
            navController = navController,
            startDestination = Route.Home,
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

            composable<Route.Home> {
                HomeScreenMain(
                    pv=pv,
                    viewModel = homeViewModel
                )
            }
            composable<Route.FundScreener> {  }
            composable<Route.PortFolio> {  }
            composable<Route.Profile> {  }

        }

    }
}