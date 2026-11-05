package org.sharad.velvetinvestment.features.fireonboarding.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.fireonboarding.data.repository.FireOnboardingRepositoryImpl
import org.sharad.velvetinvestment.features.fireonboarding.domain.repository.FireOnboardingRepository
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireGoalsViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireOnboardingViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.CurrentAssetEditViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.FinancialFlowEditScreenViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.FireReportViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.InsuranceCoverageEditViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.CASParserViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.GoalScreenOnboardingViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.OnBoardingConfirmationViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel

/**
 * The FIRE onboarding (`POST /onboarding/{step}`) and the FIRE report. `PersonalDetails`,
 * `GoalScreenOnboarding` and `OnBoardingConfirmation` view models belong to the old questionnaire
 * and are no longer opened.
 */
val fireOnboardingModule = module {
    single<FireOnboardingRepository> { FireOnboardingRepositoryImpl(get()) }
    viewModel { FireOnboardingViewModel(get(), get()) }
    viewModel { FireGoalsViewModel(get(), get()) }

    viewModel { CurrentAssetEditViewModel(get(), get()) }
    viewModel { FinancialFlowEditScreenViewModel(get(), get()) }
    viewModel { FireReportViewModel(get(),get()) }
    viewModel { InsuranceCoverageEditViewModel(get(), get()) }
    viewModel { CASParserViewModel(get()) }
    viewModel { CurrentAssetViewModel(get()) }
    viewModel { FinancialFlowScreenViewModel(get()) }
    viewModel { GoalScreenOnboardingViewModel() }
    viewModel { InsuranceCoverageViewModel(get()) }
    viewModel { LoanScreenViewModel(get()) }
    viewModel { OnBoardingConfirmationViewModel(get()) }
    viewModel {(idx:Int)-> PersonalDetailsScreenViewModel(idx,get(),get()) }
}
