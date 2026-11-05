package org.sharad.velvetinvestment.features.goals.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.goals.data.repository.GoalsRepositoryImpl
import org.sharad.velvetinvestment.features.goals.domain.repository.GoalsRepository
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.AddGoalViewModel
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.MapSchemeViewModel
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.ProjectedImpactViewModel
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.YourGoalsViewModel

val goalsFeatureModule = module {
    single<GoalsRepository> { GoalsRepositoryImpl(get()) }
    viewModel { YourGoalsViewModel(get()) }
    viewModel { AddGoalViewModel(get()) }
    viewModel { (id: String) -> ProjectedImpactViewModel(id, get()) }
    viewModel { (id: String) -> MapSchemeViewModel(id, get(), get()) }
}
