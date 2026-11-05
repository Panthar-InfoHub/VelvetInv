package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.features.fireonboarding.navigation.FireOnboardingRoute
import org.sharad.velvetinvestment.features.fireonboarding.navigation.route
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.currentassets.CASUploadScreenDialog
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.navigation.FireOnboardingNavigation
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.CurrentAssetEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireGoalsViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireOnboardingEffect
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireOnboardingEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireOnboardingViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireStepEffect
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.InsuranceCoverageEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.LoanScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

/**
 * The FIRE onboarding: header (step x of 5 + Skip), the nested step NavHost, and the CAS dialog.
 * Opened from the home "Complete your FIRE journey" card. [onClose] runs once every step is
 * verified or skipped, or when the user backs out; refresh events are already sent by then.
 */
@Composable
fun FireOnboardingScreenRoot(
    onClose: () -> Unit
) {
    val flowViewModel: FireOnboardingViewModel = koinViewModel()
    val flowState by flowViewModel.uiState.collectAsStateWithLifecycle()

    // Step view models live as long as the flow, so a step's data survives its add-item screen.
    val financialFlowScreenViewModel: FinancialFlowScreenViewModel = koinViewModel()
    val assetViewModel: CurrentAssetViewModel = koinViewModel()
    val assetState by assetViewModel.uiState.collectAsStateWithLifecycle()
    val loanScreenViewModel: LoanScreenViewModel = koinViewModel()
    val insuranceCoverageViewModel: InsuranceCoverageViewModel = koinViewModel()
    val goalsViewModel: FireGoalsViewModel = koinViewModel()

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val onAddItemScreen = backStackEntry?.destination?.let {
        it.hasRoute(FireOnboardingRoute.AddLoan::class) || it.hasRoute(FireOnboardingRoute.GoalAdd::class)
    } == true

    LaunchedEffect(flowViewModel) {
        flowViewModel.effect.collect { effect ->
            when (effect) {
                // A saved or skipped step is never revisited: each step replaces the last.
                is FireOnboardingEffect.NavigateToStage -> navController.navigate(effect.stage.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                    launchSingleTop = true
                }
                FireOnboardingEffect.Close -> onClose()
            }
        }
    }

    LaunchedEffect(Unit) {
        val steps: List<Flow<FireStepEffect>> = listOf(
            financialFlowScreenViewModel.effect,
            assetViewModel.effect,
            loanScreenViewModel.effect,
            insuranceCoverageViewModel.effect,
            goalsViewModel.stepEffect
        )
        merge(*steps.toTypedArray()).collect { effect ->
            when (effect) {
                FireStepEffect.Submitted -> flowViewModel.handleEvent(FireOnboardingEvent.OnStepSubmitted)
            }
        }
    }

    Scaffold(
        containerColor = Color.White
    ) { pv ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                OnBoardingHeader(
                    currentStep = flowState.stepNumber,
                    totalStep = flowState.totalSteps,
                    showSkip = flowState.stage != null && !flowState.isAdvancing && !onAddItemScreen,
                    onSkip = { flowViewModel.handleEvent(FireOnboardingEvent.OnSkipClicked) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    FireOnboardingNavigation(
                        flowState = flowState,
                        flowViewModel = flowViewModel,
                        financialFlowScreenViewModel = financialFlowScreenViewModel,
                        assetViewModel = assetViewModel,
                        loanScreenViewModel = loanScreenViewModel,
                        insuranceCoverageViewModel = insuranceCoverageViewModel,
                        goalsViewModel = goalsViewModel,
                        pv = pv,
                        navController = navController
                    )
                }
            }
            if (assetState.showCasDialog) {
                CASUploadScreenDialog(
                    hideDialog = { assetViewModel.handleEvent(CurrentAssetEvent.OnCasDialogDismissed) },
                    onSuccess = { report ->
                        assetViewModel.handleEvent(CurrentAssetEvent.OnCasImported(report))
                        insuranceCoverageViewModel.handleEvent(
                            InsuranceCoverageEvent.OnLifeInsuranceChanged(
                                report.summary.accounts.insurance.total_value.toLong().toString()
                            )
                        )
                    }
                )
            }
        }
    }
}
