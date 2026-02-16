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
import androidx.navigation.toRoute
import org.sharad.velvetinvestment.presentation.portfolio.compose.CancelSIPConfirmationScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.FDDetailsScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.SIPCancellationReasonScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.SIPDetailsScreen

@Composable
fun AppNavigation(){

    Scaffold(
        containerColor = Color.White
    ) {
        val pv=it
        val navController= rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.BottomNav,
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

            composable<Route.BottomNav> {
                BottomNavigation(
                    navigateToSIPDetailsScreen = {id->navController.navigate(Route.SIPDetails(id))},
                    navigateToFDDetailsScreen = {id->navController.navigate(Route.FDDetailsScreen(id))}
                )
            }

            composable<Route.SIPDetails> {
                val id= it.toRoute<Route.SIPDetails>().id
                SIPDetailsScreen(
                    onBackClick = {navController.popBackStack()},
                    onCancelClick={navController.navigate(Route.SIPCancellationScreen(it))},
                    id=id
                )
            }
            composable<Route.SIPCancellationScreen> {
                val id= it.toRoute<Route.SIPCancellationScreen>().id
                CancelSIPConfirmationScreen(
                    id=id,
                    onConfirmClick = {id->navController.navigate(Route.CancelSIPReason(id))},
                    onCancelClick = {navController.popBackStack()},
                    pv=pv
                )
            }
            composable<Route.CancelSIPReason> {
                val id= it.toRoute<Route.CancelSIPReason>().id
                SIPCancellationReasonScreen(
                    id=id,
                    onConfirmClick = {},
                    onBackClick = {navController.popBackStack()},
                    pv=pv
                )
            }

            composable<Route.FDDetailsScreen> {
                val id= it.toRoute<Route.FDDetailsScreen>().id
                FDDetailsScreen(
                    id = id,
                    onBackClick = { navController.popBackStack() },
                    pv = pv,
                )
            }

        }

    }
}