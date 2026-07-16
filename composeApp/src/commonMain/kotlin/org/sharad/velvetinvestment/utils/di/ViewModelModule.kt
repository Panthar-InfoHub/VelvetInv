package org.sharad.velvetinvestment.utils.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.presentation.LoginScreen.viewmodel.LoginScreenViewModel
import org.sharad.velvetinvestment.presentation.explorefunds.viewmodel.ExploreFundScreenViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.CurrentAssetEditViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.FinancialFlowEditScreenViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.FireReportViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.InsuranceCoverageEditViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDDetailsViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDPurchaseViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDSearchResultViewModel
import org.sharad.velvetinvestment.presentation.goals.viewmodel.GoalInfoScreenViewModel
import org.sharad.velvetinvestment.presentation.goals.viewmodel.ProjectionImpactViewModel
import org.sharad.velvetinvestment.presentation.goals.viewmodel.SingleGoalViewModel
import org.sharad.velvetinvestment.presentation.homescreen.HomeScreenViewModel
import org.sharad.velvetinvestment.presentation.insurance.InsuranceScreenViewModel
import org.sharad.velvetinvestment.presentation.insurance.viewmodel.InsuranceViewModel
import org.sharad.velvetinvestment.presentation.investmentrate.viewmodel.InvestmentRateScreenViewModel
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCFormScreenViewModel
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCImageUploaderScreenViewModel
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KYCScreenViewModel
import org.sharad.velvetinvestment.presentation.kyc.viewmodels.KycContractViewModel
import org.sharad.velvetinvestment.presentation.cart.viewmodel.CartScreenViewModel
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.AllBundlesViewModel
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.BundleDetailsViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundDetailsScreenViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundSearchResultViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.CategoryMutualFundViewModel
import org.sharad.velvetinvestment.presentation.loans.viewmodel.AddLoanViewModel
import org.sharad.velvetinvestment.presentation.loans.viewmodel.LoanInfoViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CASParserViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.GoalScreenOnboardingViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.OnBoardingConfirmationViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.PersonalDetailsScreenViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.ExistingFundsLumpSumViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.FDPortFolioDetailsViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.PortfolioScreenViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.FolioFundsMFViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.MFPortfolioDetailsViewModel
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel.CheckKYCViewModel
import org.sharad.velvetinvestment.presentation.settingscreens.viewmodels.SettingViewModel
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel.PersonalInfoViewModel
import org.sharad.velvetinvestment.presentation.profile.viewModel.NotificationViewModel
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel

val viewModelModule= module {
    viewModel { LoginScreenViewModel(get(), get()) }

    viewModel {(idx:Int)-> PersonalDetailsScreenViewModel(idx,get(),get()) }
    viewModel { FinancialFlowScreenViewModel() }
    viewModel { CurrentAssetViewModel() }
    viewModel { CASParserViewModel(get()) }
    viewModel { LoanScreenViewModel() }
    viewModel { LoanInfoViewModel(get(), get()) }
    viewModel { (id: String?) -> AddLoanViewModel(id, get(), get(), get()) }
    viewModel { InsuranceCoverageViewModel() }
    viewModel { GoalScreenOnboardingViewModel() }

    viewModel { HomeScreenViewModel(get(),get(),get()) }
    viewModel { PersonalInfoViewModel(get()) }

    viewModel {
        PortfolioScreenViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(), get(), get()
        )
    }

    viewModel { MFPortfolioDetailsViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel {(id:String)-> FDPortFolioDetailsViewModel(get(), get(),get(), fdId = id) }
    viewModel { ExploreFundScreenViewModel(get(), get()) }
    viewModel { CategoryMutualFundViewModel(get()) }
    viewModel {(search:String?,fundCategory:String?)-> MutualFundSearchResultViewModel(search=search,fundCategory=fundCategory,getMutualFundSearchResultUseCase = get()) }
    viewModel { (id: String) ->
        MutualFundDetailsScreenViewModel(
            id = id,
            getDetailsUseCase = get(),
            getGraphUseCase = get(),
            get(),
            get(),
            get()
        )
    }

    viewModel { FireReportViewModel(get(),get()) }
    viewModel { GoalInfoScreenViewModel(get()) }
    viewModel {(search:String)->FDSearchResultViewModel(search=search,get()) }
    viewModel { NotificationViewModel(get(), get()) }
    viewModel { CheckKYCViewModel(get()) }
    viewModel { SettingViewModel() }
    viewModel{ TradingAccountViewModel(get(),get(),get(),get()) }
    viewModel { OnBoardingConfirmationViewModel(get()) }
    viewModel{ KYCScreenViewModel(get()) }
    viewModel{ KYCFormScreenViewModel(get(),get(),get()) }
    viewModel{ KYCImageUploaderScreenViewModel(get(),get(),get()) }
    viewModel { KycContractViewModel(get(), get(), get()) }
    viewModel {(id:String)-> FDDetailsViewModel(id,get()) }
    viewModel {(id:String)-> FDPurchaseViewModel(id,get(),get()) }
    viewModel { CartScreenViewModel(get(),get(),get(),get(),get(),get(), get()) }
    viewModel { SingleGoalViewModel(get(), get()) }
    viewModel { (id: String) -> ProjectionImpactViewModel(get(),get(), get(),get(),get(),get(),id) }
    viewModel { FinancialFlowEditScreenViewModel(get(), get()) }
    viewModel { InsuranceCoverageEditViewModel(get(), get()) }
    viewModel { CurrentAssetEditViewModel(get(), get()) }
    viewModel { InsuranceScreenViewModel(get()) }
    viewModel { InsuranceViewModel(get()) }
    viewModel { AllBundlesViewModel(get()) }
    viewModel { (bundleKey: String) -> BundleDetailsViewModel(bundleKey, get(), get(), get(), get()) }
    viewModel { (folioId: String) -> FolioFundsMFViewModel(folioId, get()) }
    viewModel { InvestmentRateScreenViewModel(get()) }
    viewModel { ExistingFundsLumpSumViewModel(get(),get())}
}