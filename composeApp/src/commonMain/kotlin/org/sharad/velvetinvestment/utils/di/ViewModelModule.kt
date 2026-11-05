package org.sharad.velvetinvestment.utils.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.presentation.homescreen.HomeScreenViewModel
import org.sharad.velvetinvestment.presentation.insurance.InsuranceScreenViewModel
import org.sharad.velvetinvestment.presentation.insurance.viewmodel.InsuranceViewModel
import org.sharad.velvetinvestment.presentation.investmentrate.viewmodel.InvestmentRateScreenViewModel
import org.sharad.velvetinvestment.presentation.loans.viewmodel.AddLoanViewModel
import org.sharad.velvetinvestment.presentation.loans.viewmodel.LoanInfoViewModel
import org.sharad.velvetinvestment.presentation.settingscreens.viewmodels.SettingViewModel
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel.PersonalInfoViewModel
import org.sharad.velvetinvestment.presentation.profile.viewModel.NotificationViewModel
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel

val viewModelModule= module {

    viewModel { LoanInfoViewModel(get(), get()) }
    viewModel { (id: String?) -> AddLoanViewModel(id, get(), get(), get()) }

    viewModel { HomeScreenViewModel(get(),get(),get(),get()) }
    viewModel { PersonalInfoViewModel(get()) }

    // Portfolio view models now live in features/portfolio (portfolioFeatureModule); the old
    // ones are parked in composeApp/later/.
    // Explore tab + MF list/search/details view models now live in features/bottomNavigation and
    // features/mutualfund (their own Koin modules); the old ones are parked in composeApp/later/.

    viewModel { NotificationViewModel(get(), get()) }
    viewModel { SettingViewModel() }
    viewModel{ TradingAccountViewModel(get(),get(),get(),get()) }
    viewModel { InsuranceScreenViewModel(get()) }
    viewModel { InsuranceViewModel(get()) }
    viewModel { InvestmentRateScreenViewModel(get()) }
    // TODO(top-up): viewModel { ExistingFundsLumpSumViewModel(get(),get())} — parked in composeApp/later/.
}