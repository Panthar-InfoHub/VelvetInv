package org.sharad.velvetinvestment.features.bottomNavigation.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.bottomNavigation.ui.viewmodels.ExploreFundsViewModel

/** JN's bottomNavigation feature — only the Explore tab is ported so far. */
val bottomNavigationFeatureModule = module {
    viewModel { ExploreFundsViewModel(get(), get()) }
}
