package org.sharad.velvetinvestment.presentation.goals.compose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.presentation.goals.viewmodel.ProjectionImpactViewModel
import org.sharad.velvetinvestment.shared.Navigation.Route

@Composable
fun GoalProjectionNavigation(
    goalId: String,
    onBack: () -> Unit,
    onInvestNow: () -> Unit,
    navigateToAllBundles: () -> Unit,
    navigateToSpecificBundle: (String) -> Unit,
    onEditGoal: (String) -> Unit = {}
){

    val navController = rememberNavController()
    val viewModel: ProjectionImpactViewModel = koinViewModel(parameters = { parametersOf(goalId) })

    NavHost(
        navController = navController,
        startDestination = Route.GoalProjectionImpact(id = goalId),
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

        composable<Route.GoalProjectionImpact> {
            val id = it.toRoute<Route.GoalProjectionImpact>().id
            ProjectionImpactScreen(
                goalId = id,
                viewModel=viewModel,
                onBack = onBack,
                onInvestNow = onInvestNow,
                navigateToAllBundles = navigateToAllBundles,
                navigateToSpecificBundle = navigateToSpecificBundle,
                onEditGoal = onEditGoal,
                onMapClick={
                    navController.navigate(Route.GoalMapScreen){
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable<Route.GoalMapScreen> { 
            MapSchemesScreen(
                onBack = {
                    navController.popBackStack()
                },
                viewModel=viewModel
            )
        }

    }

}