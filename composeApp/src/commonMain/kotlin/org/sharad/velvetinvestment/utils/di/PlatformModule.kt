package org.sharad.velvetinvestment.utils.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.sharad.velvetinvestment.features.bottomNavigation.di.bottomNavigationFeatureModule
import org.sharad.velvetinvestment.features.orders.di.ordersFeatureModule
import org.sharad.velvetinvestment.features.insurance.di.insuranceFeatureModule
import org.sharad.velvetinvestment.features.investmentrate.di.investmentRateFeatureModule
import org.sharad.velvetinvestment.features.loans.di.loansFeatureModule
import org.sharad.velvetinvestment.features.notifications.di.notificationsFeatureModule
import org.sharad.velvetinvestment.features.profile.di.profileFeatureModule
import org.sharad.velvetinvestment.features.portfolio.di.portfolioFeatureModule
import org.sharad.velvetinvestment.features.bundles.di.bundlesModule
import org.sharad.velvetinvestment.features.cart.di.cartFeatureModule
import org.sharad.velvetinvestment.features.fd.di.fdFeatureModule
import org.sharad.velvetinvestment.features.goals.di.goalsFeatureModule
import org.sharad.velvetinvestment.features.fireonboarding.di.fireOnboardingModule
import org.sharad.velvetinvestment.features.onboarding.di.onboardingModule
import org.sharad.velvetinvestment.features.login.di.loginModule
import org.sharad.velvetinvestment.features.auth.di.authModule
import org.sharad.velvetinvestment.features.mutualfund.di.mutualFundFeatureModule
import org.sharad.velvetinvestment.features.plans.di.plansFeatureModule
import org.sharad.velvetinvestment.features.search.di.searchFeatureModule

expect val platformModule: Module

fun initializeKoin(
    config: (KoinApplication.()-> Unit)?=null
){
    startKoin {
        config?.invoke(this)
        modules(
            listOf(
                platformModule,
                viewModelModule,
                repositoryModule,
                useCaseModule,
                // Ported from Janta Nivesh
                bundlesModule,
                cartFeatureModule,
                fdFeatureModule,
                goalsFeatureModule,
                mutualFundFeatureModule,
                searchFeatureModule,
                bottomNavigationFeatureModule,
                ordersFeatureModule,
                insuranceFeatureModule,
                investmentRateFeatureModule,
                loansFeatureModule,
                notificationsFeatureModule,
                profileFeatureModule,
                portfolioFeatureModule,
                plansFeatureModule,
                fireOnboardingModule,
                onboardingModule,
                loginModule,
                authModule
            )
        )
    }
}