package org.sharad.velvetinvestment.features.onboarding.di

import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.OnboardingStageLoaderViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.BasicDetailsViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.PanVerificationViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.EmailIdViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.EmailOtpViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.KycSplashViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.VerifyBankAccountViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.VerifyWithDigilockerViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.UploadSignatureViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.ReviewProfileViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.AddNomineeViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.SetupAutopayViewModel
import org.sharad.velvetinvestment.features.onboarding.data.repository.OnboardingRepoImpl
import org.sharad.velvetinvestment.features.onboarding.domain.repository.OnboardingRepo
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.RequestEmailOtpUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.VerifyEmailOtpUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.GetPANVerificationStatusUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.InitiatePANVerificationUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.InitiateKycFormUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.GetKycFormStatusUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.UploadKycFormSignatureUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.CreateMandateUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.ConfirmMandateUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.SubmitPennyDropUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.GetPennyDropStatusUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.SubmitInvestorProfileUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.GetCityByPincodeUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.SubmitNomineesUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.GetPrefilledBankDetailsUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.InitiateReversePennyDropUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.GetReversePennyDropStatusUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.SkipPANVerificationUseCase
import org.sharad.velvetinvestment.features.onboarding.domain.usecases.SubmitBasicDetailsUseCase

/**
 * The Janta Nivesh KYC onboarding (Velvet's onboarding from 2026-10-08). View model wiring as in
 * JN's `ViewModelModules.kt`.
 */
val onboardingModule = module {
    single<OnboardingRepo> { OnboardingRepoImpl(get(), get()) }

    factory { RequestEmailOtpUseCase(get()) }
    factory { VerifyEmailOtpUseCase(get()) }
    factory { GetPANVerificationStatusUseCase(get()) }
    factory { InitiatePANVerificationUseCase(get()) }
    factory { InitiateKycFormUseCase(get()) }
    factory { GetKycFormStatusUseCase(get()) }
    factory { UploadKycFormSignatureUseCase(get()) }
    factory { CreateMandateUseCase(get()) }
    factory { ConfirmMandateUseCase(get()) }
    factory { SubmitPennyDropUseCase(get()) }
    factory { GetPennyDropStatusUseCase(get()) }
    factory { SubmitInvestorProfileUseCase(get()) }
    factory { GetCityByPincodeUseCase(get()) }
    factory { SubmitNomineesUseCase(get()) }
    factory { GetPrefilledBankDetailsUseCase(get()) }
    factory { InitiateReversePennyDropUseCase(get()) }
    factory { GetReversePennyDropStatusUseCase(get()) }
    factory { SkipPANVerificationUseCase(get()) }
    factory { SubmitBasicDetailsUseCase(get()) }

    viewModel { OnboardingStageLoaderViewModel(get()) }
    viewModel { BasicDetailsViewModel(get(), get()) }
    viewModel { PanVerificationViewModel(get(), get(), get()) }
    viewModel { EmailIdViewModel(get(), get()) }
    viewModel { (email: String) -> EmailOtpViewModel(email, get(), get(), get()) }
    viewModel { KycSplashViewModel() }
    viewModel { VerifyBankAccountViewModel(get(), get(), get(), get(), get()) }
    viewModel { VerifyWithDigilockerViewModel(get(), get()) }
    viewModel { UploadSignatureViewModel(get()) }
    viewModel { ReviewProfileViewModel(get(), get(), get(), get(), get()) }
    viewModel { AddNomineeViewModel(get()) }
    viewModel { SetupAutopayViewModel(get(), get()) }
}
