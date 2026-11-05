package org.sharad.velvetinvestment.features.bundles.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.bundles.data.repository.BundlesRepo
import org.sharad.velvetinvestment.features.bundles.domain.repository.BundlesRepository
import org.sharad.velvetinvestment.features.bundles.domain.usecases.GetAllBundlesUseCase
import org.sharad.velvetinvestment.features.bundles.domain.usecases.GetBundleDetailsUseCase
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.AllBundlesViewModel
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.BundleDetailsViewModel
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode

val bundlesModule = module {
    single<BundlesRepository> { BundlesRepo(get()) }

    factory { GetAllBundlesUseCase(get()) }
    factory { GetBundleDetailsUseCase(get()) }

    viewModel { AllBundlesViewModel(get()) }
    viewModel { (bundleId: String, purchaseMode: PurchaseMode) ->
        BundleDetailsViewModel(bundleId, purchaseMode, get(), get(), get(), get(), get())
    }
}
