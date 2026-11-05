package org.sharad.velvetinvestment.features.cart.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.cart.data.repository.CartRepo
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository
import org.sharad.velvetinvestment.features.cart.domain.usecases.AddBundleToCartLumpsumUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.AddBundleToCartSipUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.AddToCartLumpsumUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.AddToCartSipUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.CheckSipPurchaseStatusUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.CheckoutCartLumpsumUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.CheckoutCartSipUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.ClearCartUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.ConfirmCartLumpsumCheckoutUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.DeleteCartItemUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.GetCartPaymentStatusUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.GetUserCartUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.InitiateSipPurchaseUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.PurchaseLumpsumFundUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.PurchaseSipFundUseCase
import org.sharad.velvetinvestment.features.cart.domain.usecases.VerifyCartSipCheckoutOtpUseCase
import org.sharad.velvetinvestment.features.cart.presentation.viewmodel.CartViewModel

val cartFeatureModule = module {
    single<CartRepository> { CartRepo(get()) }

    factory { AddBundleToCartLumpsumUseCase(get()) }
    factory { AddBundleToCartSipUseCase(get()) }
    factory { AddToCartLumpsumUseCase(get()) }
    factory { AddToCartSipUseCase(get()) }
    factory { CheckSipPurchaseStatusUseCase(get()) }
    factory { CheckoutCartLumpsumUseCase(get()) }
    factory { CheckoutCartSipUseCase(get()) }
    factory { ClearCartUseCase(get()) }
    factory { ConfirmCartLumpsumCheckoutUseCase(get()) }
    factory { DeleteCartItemUseCase(get()) }
    factory { GetCartPaymentStatusUseCase(get()) }
    factory { GetUserCartUseCase(get()) }
    factory { InitiateSipPurchaseUseCase(get()) }
    factory { PurchaseLumpsumFundUseCase(get()) }
    factory { PurchaseSipFundUseCase(get()) }
    factory { VerifyCartSipCheckoutOtpUseCase(get()) }

    viewModel { CartViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}
