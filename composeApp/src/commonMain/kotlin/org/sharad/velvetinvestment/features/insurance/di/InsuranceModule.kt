package org.sharad.velvetinvestment.features.insurance.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.insurance.data.repository.InsuranceRepo
import org.sharad.velvetinvestment.features.insurance.domain.repository.InsuranceRepository
import org.sharad.velvetinvestment.features.insurance.domain.usecase.RequestConnectionUseCase
import org.sharad.velvetinvestment.features.insurance.ui.viewmodel.InsuranceScreenViewModel
import org.sharad.velvetinvestment.features.insurance.ui.viewmodel.InsuranceViewModel

val insuranceFeatureModule = module {
    single<InsuranceRepository> { InsuranceRepo(get()) }
    factory { RequestConnectionUseCase(get()) }
    viewModel { InsuranceScreenViewModel(get()) }
    viewModel { InsuranceViewModel(get()) }
}
