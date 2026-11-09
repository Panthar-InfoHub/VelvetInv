package org.sharad.velvetinvestment.features.investmentrate.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.investmentrate.data.repository.InvestmentRateRepo
import org.sharad.velvetinvestment.features.investmentrate.domain.repository.InvestmentRateRepository
import org.sharad.velvetinvestment.features.investmentrate.domain.usecase.GetInvestmentRateDataUseCase
import org.sharad.velvetinvestment.features.investmentrate.ui.viewmodel.InvestmentRateScreenViewModel

val investmentRateFeatureModule = module {
    single<InvestmentRateRepository> { InvestmentRateRepo(get()) }
    factory { GetInvestmentRateDataUseCase(get()) }
    viewModel { InvestmentRateScreenViewModel(get()) }
}
