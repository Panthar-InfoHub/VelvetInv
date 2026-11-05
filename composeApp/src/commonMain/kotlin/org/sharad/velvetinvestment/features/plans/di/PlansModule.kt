package org.sharad.velvetinvestment.features.plans.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.FundPurchaseViewModel
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.SipMandateHandoff
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.SipPurchaseOtpViewModel
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.SipSetupViewModel
import org.sharad.velvetinvestment.features.plans.data.repository.PlansRepoImpl
import org.sharad.velvetinvestment.features.plans.domain.repository.PlansRepo
import org.sharad.velvetinvestment.features.plans.domain.usecases.CancelPurchasePlanUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.CreateMfPurchaseUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.CreateSipPlanUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.GetMandatesUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.GetMfPurchaseUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.GetPurchasePlanUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.GetPurchasePlansUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.GetSchemePlanUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.RequestMfPurchaseOtpUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.RequestPurchasePlanOtpUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.VerifyMfPurchaseOtpUseCase
import org.sharad.velvetinvestment.features.plans.domain.usecases.VerifyPurchasePlanOtpUseCase

val plansFeatureModule = module {
    single<PlansRepo> { PlansRepoImpl(get()) }

    factory { GetPurchasePlansUseCase(get()) }
    factory { GetPurchasePlanUseCase(get()) }
    factory { RequestPurchasePlanOtpUseCase(get()) }
    factory { VerifyPurchasePlanOtpUseCase(get()) }

    factory { GetSchemePlanUseCase(get()) }

    factory { CreateSipPlanUseCase(get()) }
    factory { CreateMfPurchaseUseCase(get()) }
    factory { GetMfPurchaseUseCase(get()) }
    factory { RequestMfPurchaseOtpUseCase(get()) }
    factory { VerifyMfPurchaseOtpUseCase(get()) }

    factory { GetMandatesUseCase(get()) }
    factory { CancelPurchasePlanUseCase(get()) }

    // The buy screen: route arguments first (product id, isin, name, subtitle, amount type and
    // the mode picked on the investment-method screen), then the use cases.
    viewModel { params ->
        FundPurchaseViewModel(
            mfProductId = params.get(0),
            isin = params.get(1),
            fundName = params.get(2),
            fundSubtitle = params.get(3),
            fundAmountType = params.get(4),
            initialMode = params.get(5),
            getSchemePlan = get(),
            getMandates = get(),
            createMandate = get(),
            createMfPurchase = get(),
            getMfPurchase = get(),
            requestMfPurchaseOtp = get(),
            verifyMfPurchaseOtp = get(),
            getUserData = get()
        )
    }
    viewModel { (handoff: SipMandateHandoff) -> SipSetupViewModel(handoff, get(), get(), get(), get()) }
    viewModel { (planId: String, schemeName: String, amount: Int, mode: PurchaseMode, installmentDay: Int) ->
        SipPurchaseOtpViewModel(planId, schemeName, amount, mode, installmentDay, get(), get())
    }
}
