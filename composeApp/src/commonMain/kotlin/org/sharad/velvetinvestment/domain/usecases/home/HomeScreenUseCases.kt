package org.sharad.velvetinvestment.domain.usecases.home

class HomeScreenUseCases(
     val getUserWorthCard: GetUserWorthCardUseCase,
     val getFireReport: GetFireReportSummaryUseCase,
     val getKycStatus: GetKycStatusUseCase
)