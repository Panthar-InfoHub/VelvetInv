package org.sharad.velvetinvestment.utils.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.homescreen.HomeScreenViewModel

val viewModelModule= module {


    viewModel { HomeScreenViewModel(get(),get(),get(),get()) }

    // Portfolio view models now live in features/portfolio (portfolioFeatureModule); the old
    // ones are parked in composeApp/later/.
    // Explore tab + MF list/search/details view models now live in features/bottomNavigation and
    // features/mutualfund (their own Koin modules); the old ones are parked in composeApp/later/.

    // TODO(top-up): viewModel { ExistingFundsLumpSumViewModel(get(),get())} — parked in composeApp/later/.
}