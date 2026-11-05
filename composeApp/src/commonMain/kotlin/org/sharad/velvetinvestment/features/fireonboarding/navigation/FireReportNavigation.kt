package org.sharad.velvetinvestment.features.fireonboarding.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.compose.CurrentAssetEditScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.compose.FinancialFlowEditScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.compose.FireReportScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.compose.InsuranceCoverageEditScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.compose.UpdateDetailsScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.CurrentAssetEditEffect
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.CurrentAssetEditViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.FinancialFlowEditEffect
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.FinancialFlowEditScreenViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.FireReportEffect
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.FireReportViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.InsuranceCoverageEditEffect
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.InsuranceCoverageEditViewModel

/**
 * The FIRE report and its edit screens, added to the main app's NavHost (`AppNavigation`); home's
 * FIRE card opens [FireOnboardingRoute.FireReport] once the FIRE onboarding is complete. The
 * finance / assets / insurance edit screens save through the FIRE onboarding step endpoints.
 *
 * Loans and goals live outside this feature, so the update-options screen reaches them through
 * [onLoansClick] and [onGoalsClick].
 */
fun NavGraphBuilder.fireReportDestinations(
    navController: NavHostController,
    onLoansClick: () -> Unit,
    onGoalsClick: () -> Unit
) {
    composable<FireOnboardingRoute.FireReport> {
        val vm: FireReportViewModel = koinViewModel()
        val state by vm.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    FireReportEffect.NavigateBack -> navController.popBackStack()
                    FireReportEffect.NavigateToUpdateOptions ->
                        navController.navigate(FireOnboardingRoute.FireReportUpdateOptions) { launchSingleTop = true }
                }
            }
        }
        FireReportScreen(state = state, onEvent = vm::handleEvent)
    }

    composable<FireOnboardingRoute.FireReportUpdateOptions> {
        UpdateDetailsScreen(
            onBackClick = { navController.popBackStack() },
            onFinancialFlowClick = {
                navController.navigate(FireOnboardingRoute.FinancialFlowEdit) { launchSingleTop = true }
            },
            onLoanClick = onLoansClick,
            onInsuranceClick = {
                navController.navigate(FireOnboardingRoute.InsuranceCoverageEdit) { launchSingleTop = true }
            },
            onAssetClick = {
                navController.navigate(FireOnboardingRoute.CurrentAssetEdit) { launchSingleTop = true }
            },
            onGoalsClick = onGoalsClick
        )
    }

    composable<FireOnboardingRoute.FinancialFlowEdit> {
        val vm: FinancialFlowEditScreenViewModel = koinViewModel()
        val state by vm.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    FinancialFlowEditEffect.NavigateBack -> navController.popBackStack()
                }
            }
        }
        FinancialFlowEditScreen(state = state, onEvent = vm::handleEvent)
    }

    composable<FireOnboardingRoute.InsuranceCoverageEdit> {
        val vm: InsuranceCoverageEditViewModel = koinViewModel()
        val state by vm.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    InsuranceCoverageEditEffect.NavigateBack -> navController.popBackStack()
                }
            }
        }
        InsuranceCoverageEditScreen(state = state, onEvent = vm::handleEvent)
    }

    composable<FireOnboardingRoute.CurrentAssetEdit> {
        val vm: CurrentAssetEditViewModel = koinViewModel()
        val state by vm.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    CurrentAssetEditEffect.NavigateBack -> navController.popBackStack()
                }
            }
        }
        CurrentAssetEditScreen(state = state, onEvent = vm::handleEvent)
    }
}
