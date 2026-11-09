package org.sharad.velvetinvestment.utils.di

import org.koin.dsl.module
import org.sharad.velvetinvestment.domain.models.mfkyc.GetContractPdfUseCase
import org.sharad.velvetinvestment.domain.usecases.LaunchBrowserUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFixedDepositsSearchResultUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetTopPickFDUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.PurchaseFDUseCase
import org.sharad.velvetinvestment.domain.usecases.home.GetFireReportSummaryUseCase
import org.sharad.velvetinvestment.domain.usecases.home.GetKycStatusUseCase
import org.sharad.velvetinvestment.domain.usecases.home.GetUserWorthCardUseCase
import org.sharad.velvetinvestment.domain.usecases.home.HomeScreenUseCases
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.FinalizeKycUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.GetDigiLockerDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.GetESingKycUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.InitiateKycUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.SubmitKycFormUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.UploadImageAndSignatureDataUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.UploadImageUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.UploadSignatureUseCase
import org.sharad.velvetinvestment.domain.usecases.portfolio.GetPendingOrdersUseCase
import org.sharad.velvetinvestment.domain.usecases.report.DownloadPdfByUrlUseCase
import org.sharad.velvetinvestment.domain.usecases.report.ExportCapitalReportUseCase
import org.sharad.velvetinvestment.domain.usecases.report.ExportPortfolioReportUseCase
import org.sharad.velvetinvestment.domain.usecases.report.ExportSoaReportUseCase
import org.sharad.velvetinvestment.domain.usecases.report.ExportTaxReportUseCase
import org.sharad.velvetinvestment.domain.usecases.user.LoginWithPasswordUseCase
import org.sharad.velvetinvestment.domain.usecases.user.OnBoardUserUseCase
import org.sharad.velvetinvestment.domain.usecases.user.SignOutUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateAssetsUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateFinanceUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateInsuranceUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateLoansUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateProfileUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.DownloadFirePdfUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFDPortfolioByIdUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFDRedirectUrlUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFireReportUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFolioFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetPortfolioUseCase

import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase

val useCaseModule= module {
    factory { GetUserDataUseCase(get()) }
    factory { LoginWithPasswordUseCase(get()) }
    factory { SignOutUseCase(get()) }
    factory { OnBoardUserUseCase(get()) }
    factory { UpdateAssetsUseCase(get()) }
    factory { UpdateFinanceUseCase(get()) }
    factory { UpdateInsuranceUseCase(get()) }
    factory { UpdateLoansUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }

    factory { GetUserWorthCardUseCase(get()) }

    factory { GetFireReportSummaryUseCase(get()) }

    factory { GetKycStatusUseCase(get()) }

    factory {
        HomeScreenUseCases(
            getUserWorthCard = get(),
            getFireReport = get(),
            getKycStatus = get()
        )
    }

    factory {
        GetFireReportUseCase(get())
    }
    factory { GetPortfolioUseCase(get()) }
    factory { GetFolioFundsUseCase(get()) }
    factory { GetFDPortfolioByIdUseCase(get()) }
    factory { GetFDRedirectUrlUseCase(get()) }
    factory { DownloadFirePdfUseCase(get(),get()) }

    factory { GetTopPickFDUseCase(get()) }
    factory { GetFixedDepositsSearchResultUseCase(get()) }

    // KYC
    factory { InitiateKycUseCase(get()) }
    factory { GetDigiLockerDetailsUseCase(get()) }
    factory { SubmitKycFormUseCase(get()) }
    factory {
        UploadImageUseCase(
            repository = get()
        )
    }
    factory {
        UploadSignatureUseCase(
            repository = get()
        )
    }
    factory {
        UploadImageAndSignatureDataUseCase(
            repository = get()
        )
    }

    factory { GetContractPdfUseCase(get()) }
    factory { FinalizeKycUseCase(get()) }
    factory { GetESingKycUseCase(get()) }

    // Browser
    factory { LaunchBrowserUseCase(get()) }

    factory { GetFDDetailsUseCase(get()) }
    factory { PurchaseFDUseCase(get()) }

    factory { GetContractPdfUseCase(get()) }


    factory { ExportCapitalReportUseCase(get()) }
    factory { ExportPortfolioReportUseCase(get()) }
    factory { ExportTaxReportUseCase(get()) }
    factory { ExportSoaReportUseCase(get()) }
    factory { DownloadPdfByUrlUseCase(get()) }
    factory { GetPendingOrdersUseCase(get()) }

}
