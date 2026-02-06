package org.sharad.velvetinvestment.utils.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.presentation.LoginScreen.viewmodel.LoginScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.FinancialFlowScreen
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel

val viewModelModule= module {
    viewModel { LoginScreenViewModel() }
    viewModel {(idx:Int)-> PersonalDetailsScreenViewModel(idx) }
    viewModel { FinancialFlowScreenViewModel() }
    viewModel { CurrentAssetViewModel() }
}