package org.sharad.velvetinvestment.features.portfolio.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.portfolio.data.repository.PortfolioRepoImpl
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.AwaitMfRedemptionUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.CancelLumpSumOrderUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.CancelSipOrderUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.CreateMfRedemptionUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.DownloadFolioSpecificReportUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.DownloadPdfByUrlUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.ExportCapitalReportUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.ExportPortfolioReportUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.ExportTaxReportUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.GetActiveSipsUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.GetFDPortfolioByIdUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.GetFDRedirectUrlUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.GetFolioFundsUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.GetPendingOrdersUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.GetPortfolioUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.InvestMoreLumpsumUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.RequestMfRedemptionOtpUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.VerifyMfRedemptionOtpUseCase
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.FDPortFolioDetailsViewModel
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.FolioFundsMFViewModel
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.MFPortfolioDetailsViewModel
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.PortfolioScreenViewModel
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemHolding
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemOtpViewModel
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemViewModel

/** JN's portfolio registrations (RepositoryModule / UseCaseModule / ViewModelModules). */
val portfolioFeatureModule = module {
    single<PortfolioRepo> { PortfolioRepoImpl(get()) }

    factory { GetPortfolioUseCase(get()) }
    factory { GetFolioFundsUseCase(get()) }
    factory { CreateMfRedemptionUseCase(get()) }
    factory { AwaitMfRedemptionUseCase(get()) }
    factory { RequestMfRedemptionOtpUseCase(get()) }
    factory { VerifyMfRedemptionOtpUseCase(get()) }
    factory { CancelSipOrderUseCase(get()) }
    factory { DownloadFolioSpecificReportUseCase(get(), get()) }
    factory { ExportTaxReportUseCase(get()) }
    factory { DownloadPdfByUrlUseCase(get()) }
    factory { GetFDRedirectUrlUseCase(get()) }
    factory { GetPendingOrdersUseCase(get()) }
    factory { GetActiveSipsUseCase(get()) }
    // TODO(top-up): only the parked folio top-up uses this.
    factory { InvestMoreLumpsumUseCase(get()) }
    factory { CancelLumpSumOrderUseCase(get()) }
    factory { GetFDPortfolioByIdUseCase(get()) }
    factory { ExportCapitalReportUseCase(get()) }
    factory { ExportPortfolioReportUseCase(get(), get()) }

    viewModel { (holding: RedeemHolding) -> RedeemViewModel(holding, get(), get(), get()) }
    viewModel { (redemptionId: String) -> RedeemOtpViewModel(redemptionId, get(), get(), get(), get()) }
    viewModel { (folioId: String) -> FolioFundsMFViewModel(folioId, get()) }
    viewModel { PortfolioScreenViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { MFPortfolioDetailsViewModel(get(), get(), get(), get()) }
    viewModel { (fdId: String) -> FDPortFolioDetailsViewModel(get(), get(), fdId) }
}
