package org.sharad.velvetinvestment.features.mutualfund.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.mutualfund.data.remote.repository.MutualFundRepo
import org.sharad.velvetinvestment.features.mutualfund.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.features.mutualfund.domain.usecases.GetCategoryMutualFundsUseCase
import org.sharad.velvetinvestment.features.mutualfund.domain.usecases.GetMutualFundDetailsUseCase
import org.sharad.velvetinvestment.features.mutualfund.domain.usecases.GetMutualFundGraphUseCase
import org.sharad.velvetinvestment.features.mutualfund.domain.usecases.GetMutualFundSearchResultUseCase
import org.sharad.velvetinvestment.features.mutualfund.domain.usecases.GetMutualFundTopPicksUseCase
import org.sharad.velvetinvestment.features.mutualfund.domain.usecases.RedeemFullFundUseCase
import org.sharad.velvetinvestment.features.mutualfund.domain.usecases.RedeemPartialFundUseCase
import org.sharad.velvetinvestment.features.mutualfund.ui.viewmodel.CategoryMutualFundViewModel
import org.sharad.velvetinvestment.features.mutualfund.ui.viewmodel.MutualFundDetailsScreenViewModel
import org.sharad.velvetinvestment.features.mutualfund.ui.viewmodel.MutualFundSearchResultViewModel

/**
 * JN's mutualfund feature. Velvet's old MF stack (`data/remote/repository/MutualFundRepo`,
 * `domain/usecases/fundusecases`) is still registered elsewhere for portfolio, cart and home.
 */
val mutualFundFeatureModule = module {
    single<MutualFundRepository> { MutualFundRepo(get()) }

    factory { GetCategoryMutualFundsUseCase(get()) }
    factory { GetMutualFundSearchResultUseCase(get()) }
    factory { GetMutualFundTopPicksUseCase(get()) }
    factory { GetMutualFundDetailsUseCase(get()) }
    factory { GetMutualFundGraphUseCase(get()) }
    factory { RedeemPartialFundUseCase(get()) }
    factory { RedeemFullFundUseCase(get()) }

    viewModel { CategoryMutualFundViewModel(get(), get()) }
    viewModel { (search: String?, tag: String?, category: String?, amountType: String?, investmentMode: String?) ->
        MutualFundSearchResultViewModel(search, tag, category, amountType, investmentMode, get(), get())
    }
    viewModel { params ->
        MutualFundDetailsScreenViewModel(
            id = params.get(0),
            isin = params.get(1),
            getDetailsUseCase = get(),
            getGraphUseCase = get(),
            loadCartUseCase = get(),
            addToCartLumpsumUseCase = get(),
            addToCartSIPUseCase = get()
        )
    }
}
