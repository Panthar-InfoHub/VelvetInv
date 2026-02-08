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
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets.CASUploadScreenDialog
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.GoalScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel
import org.sharad.velvetinvestment.shared.Navigation.OnBoardingNavigation

@Composable
fun OnboardingScreenRoot(
    onBoardingStep:Int=1,
) {



    val viewModel: PersonalDetailsScreenViewModel= koinViewModel { parametersOf(onBoardingStep) }
    val currentStep by viewModel.currentStep.collectAsStateWithLifecycle()
    val personalDetails by viewModel.personalDetails.collectAsStateWithLifecycle()

    val assetViewModel: CurrentAssetViewModel= koinViewModel()
    val shoeCASScreen by assetViewModel.showCASDialog.collectAsStateWithLifecycle()

    val loanScreenViewModel: LoanScreenViewModel=koinViewModel()
    val insuranceCoverageViewModel: InsuranceCoverageViewModel=koinViewModel()
    val goalViewModel: GoalScreenViewModel=koinViewModel()




    Scaffold(
        containerColor = Color.White
    ){pv->
        Box(modifier=Modifier.fillMaxSize())
        {
            Column(
                modifier = Modifier.fillMaxSize().padding(top = pv.calculateTopPadding() + 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                OnBoardingHeader(
                    currentStep = currentStep,
                    onSkip = {},
                    showSkip = true,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    OnBoardingNavigation(
                        personalDetailsViewModel = viewModel,
                        assetViewModel = assetViewModel,
                        pv = pv,
                        loanScreenViewModel=loanScreenViewModel,
                        insuranceCoverageViewModel=insuranceCoverageViewModel,
                        goalViewModel=goalViewModel
                    )
                }
            }
            if (shoeCASScreen){
                CASUploadScreenDialog(
                    hideDialog = { assetViewModel.hideCASDialog() }
                )
            }
        }
    }

}

