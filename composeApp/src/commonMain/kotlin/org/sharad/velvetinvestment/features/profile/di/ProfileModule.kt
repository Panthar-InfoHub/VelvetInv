package org.sharad.velvetinvestment.features.profile.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.profile.ui.viewmodel.PersonalInfoViewModel

val profileFeatureModule = module {
    viewModel { PersonalInfoViewModel(get()) }
}
