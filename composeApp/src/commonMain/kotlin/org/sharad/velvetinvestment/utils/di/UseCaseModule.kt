package org.sharad.velvetinvestment.utils.di

import org.koin.dsl.module
import org.sharad.velvetinvestment.domain.usecases.home.GetFireReportSummaryUseCase
import org.sharad.velvetinvestment.domain.usecases.home.GetGoalsSummaryUseCase
import org.sharad.velvetinvestment.domain.usecases.home.GetKycStatusUseCase
import org.sharad.velvetinvestment.domain.usecases.home.GetUserWorthCardUseCase
import org.sharad.velvetinvestment.domain.usecases.home.HomeScreenUseCases

val useCaseModule= module {
    factory { GetUserWorthCardUseCase(get()) }

    factory { GetFireReportSummaryUseCase(get()) }

    factory { GetGoalsSummaryUseCase(get()) }

    factory { GetKycStatusUseCase(get()) }

    factory {
        HomeScreenUseCases(
            getUserWorthCard = get(),
            getFireReport = get(),
            getGoalsSummary = get(),
            getKycStatus = get()
        )
    }
}