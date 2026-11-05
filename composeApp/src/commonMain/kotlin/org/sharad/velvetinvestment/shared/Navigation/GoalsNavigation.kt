package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.features.goals.ui.compose.FinancialGoalScreen
import org.sharad.velvetinvestment.features.goals.ui.compose.MapSchemesScreen
import org.sharad.velvetinvestment.features.goals.ui.compose.ProjectedImpactScreen
import org.sharad.velvetinvestment.features.goals.ui.compose.YourGoalsScreen
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.AddGoalEffect
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.AddGoalViewModel
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.MapSchemeViewModel
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.ProjectedImpactEffect
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.ProjectedImpactEvent
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.ProjectedImpactViewModel
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.YourGoalsEffect
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.YourGoalsEvent
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.YourGoalsUiData
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.YourGoalsViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.utils.SnackBarController

/** Your Goals → Add Goal / Goal Projection → Map Schemes, after JN's MainAppNavigation. */
fun NavGraphBuilder.goalsDestinations(navController: NavHostController) {

    composable<Route.GoalsScreen> {
        val vm: YourGoalsViewModel = koinViewModel()
        val uiState by vm.uiState.collectAsStateWithLifecycle()

        // Re-read on every entry, so coming back from creating or deleting a goal shows the list
        // as it is now (JN).
        LaunchedEffect(Unit) {
            vm.handleEvent(YourGoalsEvent.LoadGoals)
        }
        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    YourGoalsEffect.NavigateBack -> navController.popBackStack()
                    YourGoalsEffect.NavigateToAddGoal -> navController.navigate(Route.SingleGoalAdd) {
                        launchSingleTop = true
                    }
                    is YourGoalsEffect.NavigateToGoalDetails ->
                        navController.navigate(Route.GoalProjectionFlow(effect.goalId)) {
                            launchSingleTop = true
                        }
                    YourGoalsEffect.NavigateToInvest -> navController.navigate(Route.MutualFundSearchResult())
                }
            }
        }

        UiStateContainer(
            uiState = uiState,
            onRetry = { vm.handleEvent(YourGoalsEvent.LoadGoals) }
        ) { data: YourGoalsUiData ->
            YourGoalsScreen(
                state = data,
                handleEvent = vm::handleEvent
            )
        }
    }

    composable<Route.SingleGoalAdd> {
        val vm: AddGoalViewModel = koinViewModel()
        val state by vm.state.collectAsStateWithLifecycle()
        val loading by vm.loading.collectAsStateWithLifecycle()

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    AddGoalEffect.NavigateBack -> navController.popBackStack()
                    is AddGoalEffect.ShowError -> SnackBarController.showError(effect.message)
                }
            }
        }

        FinancialGoalScreen(
            state = state,
            loading = loading,
            handleEvent = vm::handleEvent
        )
    }

    composable<Route.GoalProjectionFlow> { entry ->
        val route = entry.toRoute<Route.GoalProjectionFlow>()
        val vm: ProjectedImpactViewModel = koinViewModel(parameters = { parametersOf(route.id) })
        val uiState by vm.uiState.collectAsStateWithLifecycle()
        val deleting by vm.deleting.collectAsStateWithLifecycle()

        // The VM loads once when built; coming back (e.g. from Map Schemes) re-reads the goal so
        // its mapped funds and progress are current.
        var returned by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            if (returned) vm.handleEvent(ProjectedImpactEvent.LoadGoalDetails) else returned = true
        }

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    ProjectedImpactEffect.NavigateBack -> navController.popBackStack()
                    ProjectedImpactEffect.NavigateToInvest -> navController.navigate(Route.MutualFundSearchResult())
                    is ProjectedImpactEffect.ShowError -> SnackBarController.showError(effect.message)
                    ProjectedImpactEffect.NavigateToMapScheme ->
                        navController.navigate(Route.MapSchemes(route.id)) {
                            launchSingleTop = true
                        }
                }
            }
        }

        ProjectedImpactScreen(
            state = uiState,
            handleEvent = vm::handleEvent,
            deleting = deleting
        )
    }

    composable<Route.MapSchemes> { entry ->
        val route = entry.toRoute<Route.MapSchemes>()
        val vm: MapSchemeViewModel = koinViewModel(parameters = { parametersOf(route.id) })
        val uiState by vm.uiState.collectAsStateWithLifecycle()

        MapSchemesScreen(
            uiState = uiState,
            effectFlow = vm.effect,
            onEvent = vm::handleEvent,
            onBack = { navController.popBackStack() }
        )
    }
}
