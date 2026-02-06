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
import org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets.CurrentAssetScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.FinancialFlowScreen
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.PersonalDetailScreen
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel

@Composable
fun OnboardingScreenRoot(
    onBoardingStep:Int=1,
) {



    val viewModel: PersonalDetailsScreenViewModel= koinViewModel { parametersOf(onBoardingStep) }
    val currentStep by viewModel.currentStep.collectAsStateWithLifecycle()
    val personalDetails by viewModel.personalDetails.collectAsStateWithLifecycle()


    Scaffold(
        containerColor = Color.White
    ){pv->
        Column(
            modifier = Modifier.fillMaxSize().padding(top = pv.calculateTopPadding()+16.dp),
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
                when (currentStep) {
                    1 -> {
                        PersonalDetailScreen(
                            onNameChange = { viewModel.onNameChange(it) },
                            onEmailChange = { viewModel.onEmailChange(it) },
                            onPhoneChange = { viewModel.onPhoneChange(it) },
                            onCityChange = { viewModel.onCityChange(it) },
                            onDobChange = { viewModel.onDobChange(it) },
                            onNext = { viewModel.nextStep() },
                            onSliderChange = { viewModel.onSliderChange(it) },
                            details = personalDetails,
                            pv = pv
                        )
                    }
                    2->{
                        FinancialFlowScreen(
                            pv = pv,
                            onNext = {viewModel.nextStep()},
                            onPrev = {viewModel.previousStep()},
                        )
                    }
                    3->{
                        CurrentAssetScreen(
                            pv = pv,
                            onNext = {viewModel.nextStep()},
                            onPrev = {viewModel.previousStep()},
                        )
                    }
                }
            }
        }
    }

}

