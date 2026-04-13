package org.sharad.velvetinvestment.utils.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.presentation.LoginScreen.viewmodel.LoginScreenViewModel
import org.sharad.velvetinvestment.presentation.explorefunds.viewmodel.ExploreFundScreenViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.CurrentAssetEditViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.FinancialFlowEditScreenViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.FireReportViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.InsuranceCoverageEditViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDCategoryViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDDetailsViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDPurchaseViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDSearchResultViewModel
import org.sharad.velvetinvestment.presentation.goals.viewmodel.GoalInfoScreenViewModel
import org.sharad.velvetinvestment.presentation.goals.viewmodel.SingleGoalViewModel
import org.sharad.velvetinvestment.presentation.homescreen.HomeScreenViewModel
import org.sharad.velvetinvestment.presentation.insurance.InsuranceScreenViewModel
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCFormScreenViewModel
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCImageUploaderScreenViewModel
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCScreenViewModel
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KycContractViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.CartScreenViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundDetailsScreenViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundSearchResultViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.CategoryMutualFundViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CASParserViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.GoalScreenOnboardingViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.OnBoardingConfirmationViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.FDPortFolioDetailsViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.PortfolioScreenViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.SIPDetailsViewModel
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel.CheckKYCViewModel
import org.sharad.velvetinvestment.presentation.settingscreens.viewmodels.SettingViewModel
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.NotificationCentreViewModel
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel

val viewModelModule= module {
    viewModel { LoginScreenViewModel(get()) }

    viewModel {(idx:Int)-> PersonalDetailsScreenViewModel(idx,get(),get()) }
    viewModel { FinancialFlowScreenViewModel() }
    viewModel { CurrentAssetViewModel() }
    viewModel { CASParserViewModel(get()) }
    viewModel { LoanScreenViewModel() }
    viewModel { InsuranceCoverageViewModel() }
    viewModel { GoalScreenOnboardingViewModel() }

    viewModel { HomeScreenViewModel(get()) }

    viewModel {
        PortfolioScreenViewModel(
            get(), // GetMutualFundsUseCase
            get(), // GetMutualFundDashboardUseCase
            get()  // GetFDListUseCase
        )
    }

    viewModel { SIPDetailsViewModel() }
    viewModel {(id:String)-> FDPortFolioDetailsViewModel(get(), fdId = id) }
    viewModel { ExploreFundScreenViewModel(get(), get()) }
    viewModel { CategoryMutualFundViewModel(get()) }
    viewModel { MutualFundSearchResultViewModel(getMutualFundSearchResultUseCase = get()) }
    viewModel { (id: String) ->
        MutualFundDetailsScreenViewModel(
            id = id,
            getDetailsUseCase = get(),
            getGraphUseCase = get(),
            get(),
            get()
        )
    }

    viewModel { FireReportViewModel(get(),get()) }
    viewModel { GoalInfoScreenViewModel(get()) }
    viewModel { FDCategoryViewModel(get()) }
    viewModel {FDSearchResultViewModel(get()) }
    viewModel { NotificationCentreViewModel() }
    viewModel { CheckKYCViewModel() }
    viewModel { SettingViewModel() }
    viewModel{ TradingAccountViewModel() }
    viewModel { OnBoardingConfirmationViewModel(get()) }
    viewModel{ KYCScreenViewModel(get(), get()) }
    viewModel{ KYCFormScreenViewModel(get(),get(),get()) }
    viewModel{ KYCImageUploaderScreenViewModel(get(),get(),get()) }
    viewModel { KycContractViewModel(get(), get(), get(), get()) }
    viewModel {(id:String)-> FDDetailsViewModel(id,get()) }
    viewModel {(id:String)-> FDPurchaseViewModel(id,get(),get(), get()) }
    viewModel { CartScreenViewModel(get(),get(),get(),get()) }
    viewModel { SingleGoalViewModel(get(), get()) }
    viewModel { FinancialFlowEditScreenViewModel(get()) }
    viewModel { InsuranceCoverageEditViewModel(get()) }
    viewModel { CurrentAssetEditViewModel(get()) }
    viewModel { InsuranceScreenViewModel(get()) }
}