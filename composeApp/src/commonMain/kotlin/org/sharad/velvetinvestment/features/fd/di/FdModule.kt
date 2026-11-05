package org.sharad.velvetinvestment.features.fd.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.fd.data.repository.FixedDepositRepo
import org.sharad.velvetinvestment.features.fd.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.features.fd.domain.usecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.features.fd.domain.usecases.GetFixedDepositsSearchResultUseCase
import org.sharad.velvetinvestment.features.fd.domain.usecases.GetTopPickFDUseCase
import org.sharad.velvetinvestment.features.fd.domain.usecases.PurchaseFDUseCase
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.ExploreFdViewModel
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.FdDetailsViewModel
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.SetInvestmentDetailsViewModel

val fdFeatureModule = module {
    single<FixedDepositRepository> { FixedDepositRepo(get()) }

    factory { GetFixedDepositsSearchResultUseCase(get()) }
    factory { GetTopPickFDUseCase(get()) }
    factory { GetFDDetailsUseCase(get()) }
    factory { PurchaseFDUseCase(get()) }

    viewModel { ExploreFdViewModel(get()) }
    viewModel { (id: String) -> FdDetailsViewModel(id, get()) }
    viewModel { (id: String) -> SetInvestmentDetailsViewModel(id, get(), get()) }
}
