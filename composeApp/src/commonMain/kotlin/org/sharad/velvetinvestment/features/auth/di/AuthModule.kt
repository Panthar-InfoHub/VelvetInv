package org.sharad.velvetinvestment.features.auth.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.auth.data.repository.MpinRepoImpl
import org.sharad.velvetinvestment.features.auth.domain.repository.MpinRepo
import org.sharad.velvetinvestment.features.auth.domain.usecase.UpdateMpinUseCase
import org.sharad.velvetinvestment.features.auth.domain.usecase.VerifyMpinUseCase
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.BiometricSettingsViewModel
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.ChangePinViewModel
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.EnterPinViewModel

val authModule = module {
    single<MpinRepo> { MpinRepoImpl(get()) }

    factory { UpdateMpinUseCase(get()) }
    factory { VerifyMpinUseCase(get()) }

    viewModel { (purpose: String) -> EnterPinViewModel(purpose, get(), get()) }
    viewModel { ChangePinViewModel(get(), get(), get(), get()) }
    viewModel { BiometricSettingsViewModel(get()) }
}
