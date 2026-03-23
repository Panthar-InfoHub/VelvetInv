package org.sharad.velvetinvestment.utils.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.presentation.LoginScreen.viewmodel.LoginScreenViewModel
import org.sharad.velvetinvestment.presentation.explorefunds.viewmodel.ExploreFundScreenViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.FireReportViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDCategoryViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDSearchResultViewModel
import org.sharad.velvetinvestment.presentation.goals.viewmodel.GoalInfoScreenViewModel
import org.sharad.velvetinvestment.presentation.homescreen.HomeScreenViewModel
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCFormScreenViewModel
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCScreenViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundDetailsScreenViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundSearchResultViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CASParserViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.GoalScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.OnBoardingConfirmationViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.FDDetailsViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.PortfolioScreenViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.SIPDetailsViewModel
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel.CheckKYCViewModel
import org.sharad.velvetinvestment.presentation.settingscreens.viewmodels.SettingViewModel
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.NotificationCentreViewModel
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel

val viewModelModule= module {
    viewModel { LoginScreenViewModel(get()) }

    viewModel {(idx:Int)-> PersonalDetailsScreenViewModel(idx,get()) }
    viewModel { FinancialFlowScreenViewModel() }
    viewModel { CurrentAssetViewModel() }
    viewModel { CASParserViewModel(get()) }
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

    viewModel { FireReportViewModel(get(),get()) }
    viewModel { GoalInfoScreenViewModel(get()) }
    viewModel { FDCategoryViewModel(get()) }
    viewModel {(id:String)-> FDSearchResultViewModel(id,get()) }
    viewModel { NotificationCentreViewModel() }
    viewModel { CheckKYCViewModel() }
    viewModel { SettingViewModel() }
    viewModel{ TradingAccountViewModel() }
    viewModel { OnBoardingConfirmationViewModel(get()) }
    viewModel{ KYCScreenViewModel(get(), get()) }
    viewModel{ KYCFormScreenViewModel(get(),get(),get()) }
}