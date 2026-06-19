package org.sharad.velvetinvestment.presentation.loans.compose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.sharad.velvetinvestment.shared.Navigation.Route

@Composable
fun LoanFlowScreen(
    onBack: () -> Unit
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.LoansScreen,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        }
    ){
        composable<Route.LoansScreen> {
            LoansScreen(
                onBack = onBack,
                onAddClick = {
                    navController.navigate(Route.AddLoanScreen())
                },
                onEditClick = { id ->
                    navController.navigate(Route.AddLoanScreen(id))
                },
            )
        }

        composable<Route.AddLoanScreen> { backStackEntry ->
            val route: Route.AddLoanScreen = backStackEntry.toRoute()
            AddLoanScreen(
                loanId = route.loanId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
