package org.sharad.velvetinvestment.presentation.onboarding.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets.CASUploadScreenDialog
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.GoalScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel
import org.sharad.velvetinvestment.shared.Navigation.OnBoardingNavigation
import org.sharad.velvetinvestment.shared.Navigation.Route

@Composable
fun OnboardingScreenRoot(
    onBoardingStep: Int = 1,
    onLoginSuccessNavigation: () -> Unit,
) {



    val viewModel: PersonalDetailsScreenViewModel= koinViewModel { parametersOf(onBoardingStep) }
    val currentStep by viewModel.currentStep.collectAsStateWithLifecycle()
    val financialFlowScreenViewModel: FinancialFlowScreenViewModel= koinViewModel()
    val assetViewModel: CurrentAssetViewModel= koinViewModel()
    val shoeCASScreen by assetViewModel.showCASDialog.collectAsStateWithLifecycle()
    val loanScreenViewModel: LoanScreenViewModel=koinViewModel()
    val insuranceCoverageViewModel: InsuranceCoverageViewModel=koinViewModel()
    val goalViewModel: GoalScreenViewModel=koinViewModel()

    val navController = rememberNavController()





    Scaffold(
        containerColor = Color.White
    ){pv->
        Box(modifier=Modifier.fillMaxSize())
        {
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                OnBoardingHeader(
                    currentStep = currentStep,
                    showSkip = if (currentStep in listOf(1,7)) false else true,
                    onSkip = {
                        when (currentStep) {
                            1 -> {
                                viewModel.nextStep()
                                navController.navigate(Route.OnBoardingFinancialFlow)
                            }
                            2 -> {
                                viewModel.nextStep()
                                navController.navigate(Route.OnBoardingCurrentAssets)
                            }
                            3 -> {
                                viewModel.nextStep()
                                navController.navigate(Route.OnBoardingLoan)
                            }
                            4 -> {
                                viewModel.nextStep()
                                navController.navigate(Route.OnBoardingInsuranceCoverage)
                            }
                            5 -> {
                                viewModel.nextStep()
                                navController.navigate(Route.OnBoardingGoal)
                            }
                            6 -> {
                                viewModel.nextStep()
                                navController.navigate(Route.OnBoardingSummary)
                            }
                            7 -> {
                            }
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    OnBoardingNavigation(
                        personalDetailsViewModel = viewModel,
                        assetViewModel = assetViewModel,
                        pv = pv,
                        loanScreenViewModel=loanScreenViewModel,
                        insuranceCoverageViewModel=insuranceCoverageViewModel,
                        goalViewModel=goalViewModel,
                        financialFlowScreenViewModel = financialFlowScreenViewModel,
                        navController = navController,
                        onLoginSuccessNavigation=onLoginSuccessNavigation
                    )
                }
            }
            if (shoeCASScreen){
                CASUploadScreenDialog(
                    hideDialog = { assetViewModel.hideCASDialog() },
                    onSuccess = {data->
                        assetViewModel.onStocksAndSharesUpdate(data.summary.accounts.demat.total_value.toLong().toString())
                        insuranceCoverageViewModel.onLifeInsuranceAmountChange(data.summary.accounts.insurance.total_value.toLong().toString())
                        assetViewModel.onMutualFundsUpdate(data.summary.accounts.mutual_funds.total_value.toLong().toString())
                    }
                )
            }
        }
    }

}

