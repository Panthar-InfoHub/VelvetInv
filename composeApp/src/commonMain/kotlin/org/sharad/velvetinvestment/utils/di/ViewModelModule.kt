package org.sharad.velvetinvestment.utils.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.presentation.LoginScreen.viewmodel.LoginScreenViewModel
import org.sharad.velvetinvestment.presentation.explorefunds.viewmodel.ExploreFundScreenViewModel
import org.sharad.velvetinvestment.presentation.homescreen.HomeScreenViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundDetailsScreenViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundSearchResultViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.GoalScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.FDDetailsViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.PortfolioScreenViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.SIPDetailsViewModel

val viewModelModule= module {
    viewModel { LoginScreenViewModel() }

    viewModel {(idx:Int)-> PersonalDetailsScreenViewModel(idx) }
    viewModel { FinancialFlowScreenViewModel() }
    viewModel { CurrentAssetViewModel() }
    viewModel { LoanScreenViewModel() }
    viewModel { InsuranceCoverageViewModel() }
    viewModel { GoalScreenViewModel() }

    viewModel { HomeScreenViewModel(get()) }

    viewModel {
        PortfolioScreenViewModel(
            get(), // GetMutualFundsUseCase
            get(), // GetMutualFundDashboardUseCase
            get()  // GetFDListUseCase
        )
    }

    viewModel { SIPDetailsViewModel() }
    viewModel {(id:String)-> FDDetailsViewModel(get(), fdId = id) }
    viewModel { ExploreFundScreenViewModel(get(), get()) }
    viewModel { MutualFundViewModel(get()) }
    viewModel { (id: String) -> MutualFundSearchResultViewModel(id = id, getMutualFundSearchResultUseCase = get()) }
    viewModel { (id: String) ->
        MutualFundDetailsScreenViewModel(
            id = id,
            getDetailsUseCase = get(),
            getGraphUseCase = get()
        )
    }

}