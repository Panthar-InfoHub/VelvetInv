package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.sharad.velvetinvestment.features.fireonboarding.navigation.FireOnboardingRoute
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.currentassets.CurrentAssetScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.financialflow.FinancialFlowScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.goals.OnBoardingGoalScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.insurancecoverage.InsuranceCoverageScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.loan.AddLoanScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.loan.OnBoardingLoanScreen
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.CurrentAssetEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FinancialFlowEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireGoalsEffect
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireGoalsEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireGoalsViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireOnboardingEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireOnboardingUiState
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireOnboardingViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.InsuranceCoverageEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.LoanEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.features.goals.ui.compose.FinancialGoalScreen
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.AddGoalEvent
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen

/**
 * The FIRE onboarding's nested NavHost. It only renders steps: which one is open is decided by
 * [FireOnboardingViewModel] (the root collects its NavigateToStage effects). A step's Continue
 * goes to its own view model, which saves it and tells the coordinator; Back leaves the flow.
 */
@Composable
fun FireOnboardingNavigation(
    flowState: FireOnboardingUiState,
    flowViewModel: FireOnboardingViewModel,
    financialFlowScreenViewModel: FinancialFlowScreenViewModel,
    assetViewModel: CurrentAssetViewModel,
    loanScreenViewModel: LoanScreenViewModel,
    insuranceCoverageViewModel: InsuranceCoverageViewModel,
    goalsViewModel: FireGoalsViewModel,
    pv: PaddingValues,
    navController: NavHostController
) {
    val financialState by financialFlowScreenViewModel.uiState.collectAsStateWithLifecycle()
    val assetState by assetViewModel.uiState.collectAsStateWithLifecycle()
    val loanState by loanScreenViewModel.uiState.collectAsStateWithLifecycle()
    val insuranceState by insuranceCoverageViewModel.uiState.collectAsStateWithLifecycle()
    val goalsState by goalsViewModel.uiState.collectAsStateWithLifecycle()

    val busy = flowState.isAdvancing
    val exit = { flowViewModel.handleEvent(FireOnboardingEvent.OnExitClicked) }

    LaunchedEffect(goalsViewModel.effect) {
        goalsViewModel.effect.collect { effect ->
            when (effect) {
                FireGoalsEffect.CloseAddGoal -> navController.popBackStack()
            }
        }
    }

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = FireOnboardingRoute.Resolving,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500, easing = FastOutSlowInEasing))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(500, easing = FastOutSlowInEasing))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(500, easing = FastOutSlowInEasing))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500, easing = FastOutSlowInEasing))
        }
    ) {
        composable<FireOnboardingRoute.Resolving> {
            val error = flowState.error
            if (error != null) {
                ErrorScreen(
                    errorMessage = error,
                    onRetryClick = { flowViewModel.handleEvent(FireOnboardingEvent.Retry) }
                )
            } else {
                LoaderScreen()
            }
        }

        composable<FireOnboardingRoute.FinancialFlow> {
            FinancialFlowScreen(
                pv = pv,
                state = financialState,
                onEvent = financialFlowScreenViewModel::handleEvent,
                onNext = { financialFlowScreenViewModel.handleEvent(FinancialFlowEvent.OnSubmitClicked) },
                onPrev = exit,
                busy = busy
            )
        }

        composable<FireOnboardingRoute.CurrentAssets> {
            CurrentAssetScreen(
                pv = pv,
                state = assetState,
                onEvent = assetViewModel::handleEvent,
                onNext = { assetViewModel.handleEvent(CurrentAssetEvent.OnSubmitClicked) },
                onPrev = exit,
                busy = busy
            )
        }

        composable<FireOnboardingRoute.Loan> {
            OnBoardingLoanScreen(
                pv = pv,
                state = loanState,
                onEvent = loanScreenViewModel::handleEvent,
                onNext = { loanScreenViewModel.handleEvent(LoanEvent.OnSubmitClicked) },
                onPrev = exit,
                onAddLoanClick = {
                    loanScreenViewModel.handleEvent(LoanEvent.OnAddLoanOpened)
                    navController.navigate(FireOnboardingRoute.AddLoan)
                },
                busy = busy
            )
        }

        composable<FireOnboardingRoute.AddLoan> {
            AddLoanScreen(
                pv = pv,
                state = loanState,
                onEvent = loanScreenViewModel::handleEvent,
                onBack = { navController.popBackStack() }
            )
        }

        composable<FireOnboardingRoute.InsuranceCoverage> {
            InsuranceCoverageScreen(
                pv = pv,
                state = insuranceState,
                onEvent = insuranceCoverageViewModel::handleEvent,
                onNext = { insuranceCoverageViewModel.handleEvent(InsuranceCoverageEvent.OnSubmitClicked) },
                onPrev = exit,
                busy = busy
            )
        }

        composable<FireOnboardingRoute.Goal> {
            OnBoardingGoalScreen(
                pv = pv,
                state = goalsState,
                onEvent = goalsViewModel::handleEvent,
                onPrev = exit,
                onAddGoalClick = {
                    goalsViewModel.handleEvent(FireGoalsEvent.OnAddGoalOpened)
                    navController.navigate(FireOnboardingRoute.GoalAdd)
                },
                busy = busy
            )
        }

        // The goals feature's add-goal screen, unchanged: same config, same live projection.
        // Saving only adds the goal to the step's list.
        composable<FireOnboardingRoute.GoalAdd> {
            FinancialGoalScreen(
                state = goalsState.addGoal,
                loading = false,
                handleEvent = { event: AddGoalEvent -> goalsViewModel.handleEvent(FireGoalsEvent.OnAddGoalEvent(event)) },
                saveText = "Add Goal"
            )
        }
    }
}
