package org.sharad.velvetinvestment.features.loans.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.loans.data.repository.LoanRepo
import org.sharad.velvetinvestment.features.loans.domain.repository.LoanRepository
import org.sharad.velvetinvestment.features.loans.domain.usecase.DeleteLoanUseCase
import org.sharad.velvetinvestment.features.loans.domain.usecase.UpdateSingleLoanUseCase
import org.sharad.velvetinvestment.features.loans.ui.viewmodel.AddLoanViewModel
import org.sharad.velvetinvestment.features.loans.ui.viewmodel.LoanInfoViewModel

val loansFeatureModule = module {
    single<LoanRepository> { LoanRepo(get()) }
    factory { DeleteLoanUseCase(get()) }
    factory { UpdateSingleLoanUseCase(get()) }
    viewModel { LoanInfoViewModel(get(), get()) }
    viewModel { (id: String?) -> AddLoanViewModel(id, get(), get(), get()) }
}
