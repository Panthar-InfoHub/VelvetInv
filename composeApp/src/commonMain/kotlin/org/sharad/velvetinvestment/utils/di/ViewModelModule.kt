package org.sharad.velvetinvestment.utils.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.presentation.LoginScreen.viewmodel.LoginScreenViewModel

val viewModelModule= module {
    viewModel { LoginScreenViewModel() }
}