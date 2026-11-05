package org.sharad.velvetinvestment.features.login.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.login.data.repository.UserAuthenticationRepo
import org.sharad.velvetinvestment.features.login.domain.repository.UserAuth
import org.sharad.velvetinvestment.features.login.domain.usecases.LoginWithNumberUseCase
import org.sharad.velvetinvestment.features.login.domain.usecases.VerifyOTPUseCase
import org.sharad.velvetinvestment.features.login.ui.viewmodels.LoginScreenViewModel

val loginModule = module {
    single<UserAuth> { UserAuthenticationRepo(get(), get(), get(), get()) }

    factory { LoginWithNumberUseCase(get()) }
    factory { VerifyOTPUseCase(get()) }

    viewModel { LoginScreenViewModel(get(), get()) }
}
