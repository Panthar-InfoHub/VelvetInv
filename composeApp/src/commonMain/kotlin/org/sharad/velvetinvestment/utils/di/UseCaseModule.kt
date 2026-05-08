package org.sharad.velvetinvestment.utils.di

import org.koin.dsl.module
import org.sharad.velvetinvestment.domain.models.mfkyc.GetContractPdfUseCase
import org.sharad.velvetinvestment.domain.usecases.LaunchBrowserUseCase
import org.sharad.velvetinvestment.domain.usecases.fdportfoliousecases.GetFDPortFolioDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.fdportfoliousecases.GetFDListUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetBundleFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFixedDepositsSearchResultUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetTopPickFDUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.PurchaseFDUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.AddBundleToCartLumpsumUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.AddBundleToCartSipUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.AddToCartLumpsumUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.AddToCartSipUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.DeleteCartItemUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetCategoryMutualFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetCombinedCategoryMutualFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundDashboardUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundGraphUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundSearchResultUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundTopPicksUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetPortfolioMutualFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetUserCartUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.PurchaseLumpsumFundUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.PurchaseSipFundUseCase
import org.sharad.velvetinvestment.domain.usecases.home.GetFireReportSummaryUseCase
import org.sharad.velvetinvestment.domain.usecases.home.GetGoalsSummaryUseCase
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
import org.sharad.velvetinvestment.domain.usecases.mutualfunds.GetAllBundledFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.user.GetUserDataUseCase
import org.sharad.velvetinvestment.domain.usecases.user.GetUserPersonalInfo
import org.sharad.velvetinvestment.domain.usecases.user.LoginWithNumberUseCase
import org.sharad.velvetinvestment.domain.usecases.user.LoginWithPasswordUseCase
import org.sharad.velvetinvestment.domain.usecases.user.OnBoardUserUseCase
import org.sharad.velvetinvestment.domain.usecases.user.SignOutUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateAssetsUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateFinanceUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateGoalsUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateInsuranceUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateLoansUseCase
import org.sharad.velvetinvestment.domain.usecases.user.UpdateProfileUseCase
import org.sharad.velvetinvestment.domain.usecases.user.SubmitTradingAccountFormUseCase
import org.sharad.velvetinvestment.domain.usecases.user.TradingAccountConfirmationUseCase
import org.sharad.velvetinvestment.domain.usecases.user.VerifyOtpUseCase
import org.sharad.velvetinvestment.domain.usecases.user.VerifyPANUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.DownloadFirePdfUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFDPortfolioByIdUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFDRedirectUrlUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFireReportUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetInvestmentRateDataUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetPortfolioUseCase

val useCaseModule= module {
    factory { LoginWithNumberUseCase(get()) }
    factory { VerifyOtpUseCase(get()) }
    factory { LoginWithPasswordUseCase(get()) }
    factory { SignOutUseCase(get()) }
    factory { OnBoardUserUseCase(get()) }
    factory { UpdateAssetsUseCase(get()) }
    factory { UpdateFinanceUseCase(get()) }
    factory { UpdateInsuranceUseCase(get()) }
    factory { UpdateLoansUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }
    factory { UpdateGoalsUseCase(get()) }
    factory { GetUserDataUseCase(get()) }
    factory { VerifyPANUseCase(get()) }
    factory { SubmitTradingAccountFormUseCase(get()) }
    factory { TradingAccountConfirmationUseCase(get()) }

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
        GetPortfolioMutualFundsUseCase(get())
    }

    factory {
        GetMutualFundDashboardUseCase(get())
    }

    factory {
        GetFDListUseCase(get())
    }
    factory { GetFDPortFolioDetailsUseCase(get()) }

    factory { GetMutualFundTopPicksUseCase(get()) }
    factory { GetCategoryMutualFundsUseCase(get()) }
    factory { GetCombinedCategoryMutualFundsUseCase(get()) }
    factory { GetMutualFundSearchResultUseCase(get()) }
    factory { GetMutualFundDetailsUseCase(get()) }
    factory {
        GetMutualFundGraphUseCase(get())
    }
    factory {
        GetFireReportUseCase(get())
    }
    factory { GetPortfolioUseCase(get()) }
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

    factory { GetUserPersonalInfo(get()) }
    factory { org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFDDetailsUseCase(get()) }
    factory { PurchaseFDUseCase(get()) }

    factory { GetUserCartUseCase(get()) }
    factory { DeleteCartItemUseCase(get()) }
    factory { PurchaseLumpsumFundUseCase(get()) }
    factory { PurchaseSipFundUseCase(get()) }
    factory { AddToCartSipUseCase(get()) }
    factory { AddToCartLumpsumUseCase(get()) }
    factory { AddBundleToCartLumpsumUseCase(get()) }
    factory { AddBundleToCartSipUseCase(get()) }
    factory { GetBundleFundsUseCase(get()) }
    factory { GetAllBundledFundsUseCase(get()) }
    factory { GetInvestmentRateDataUseCase(get()) }

}