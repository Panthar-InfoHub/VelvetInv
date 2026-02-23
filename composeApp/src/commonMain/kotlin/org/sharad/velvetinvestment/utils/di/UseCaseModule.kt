package org.sharad.velvetinvestment.utils.di

import org.koin.dsl.module
import org.sharad.velvetinvestment.domain.usecases.fdusecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.fdusecases.GetFDListUseCase
import org.sharad.velvetinvestment.domain.usecases.fdusecases.GetFixedDepositTopPicksUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetCategoryMutualFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundDashboardUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundGraphUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundSearchResultUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundTopPicksUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundsUseCase
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


    factory {
        GetMutualFundsUseCase(get())
    }

    factory {
        GetMutualFundDashboardUseCase(get())
    }

    factory {
        GetFDListUseCase(get())
    }
    factory { GetFDDetailsUseCase(get()) }

    factory { GetMutualFundTopPicksUseCase(get()) }
    factory { GetFixedDepositTopPicksUseCase(get()) }
    factory { GetCategoryMutualFundsUseCase(get()) }
    factory { GetMutualFundSearchResultUseCase(get()) }
    factory { GetMutualFundDetailsUseCase(get()) }
    factory {
        GetMutualFundGraphUseCase(get())
    }
}