package org.sharad.velvetinvestment.features.search.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.search.data.repository.RecentSearchRepoImpl
import org.sharad.velvetinvestment.features.search.domain.repository.RecentSearchRepo
import org.sharad.velvetinvestment.features.search.domain.usecases.ClearRecentSearchesUseCase
import org.sharad.velvetinvestment.features.search.domain.usecases.DeleteRecentSearchUseCase
import org.sharad.velvetinvestment.features.search.domain.usecases.ObserveRecentSearchesUseCase
import org.sharad.velvetinvestment.features.search.domain.usecases.SaveRecentSearchUseCase
import org.sharad.velvetinvestment.features.search.ui.viewmodels.SearchOverlayViewModel
import org.sharad.velvetinvestment.shared.data.local.database.VelvetDatabase

/** The search overlay and its recent searches. [VelvetDatabase] comes from the platform module. */
val searchFeatureModule = module {
    single { get<VelvetDatabase>().recentSearchDao() }
    single<RecentSearchRepo> { RecentSearchRepoImpl(get()) }

    factory { ObserveRecentSearchesUseCase(get()) }
    factory { SaveRecentSearchUseCase(get()) }
    factory { DeleteRecentSearchUseCase(get()) }
    factory { ClearRecentSearchesUseCase(get()) }

    viewModel { SearchOverlayViewModel(get(), get(), get(), get(), get()) }
}
