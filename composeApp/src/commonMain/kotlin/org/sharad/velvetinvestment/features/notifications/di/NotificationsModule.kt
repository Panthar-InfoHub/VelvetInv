package org.sharad.velvetinvestment.features.notifications.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.notifications.data.repository.NotificationRepo
import org.sharad.velvetinvestment.features.notifications.domain.repository.NotificationRepository
import org.sharad.velvetinvestment.features.notifications.domain.usecase.GetNotificationsUseCase
import org.sharad.velvetinvestment.features.notifications.domain.usecase.GetUnreadStatusUseCase
import org.sharad.velvetinvestment.features.notifications.domain.usecase.MarkNotificationsAsReadUseCase
import org.sharad.velvetinvestment.features.notifications.ui.viewmodel.NotificationViewModel

val notificationsFeatureModule = module {
    single<NotificationRepository> { NotificationRepo(get()) }
    factory { GetNotificationsUseCase(get()) }
    factory { GetUnreadStatusUseCase(get()) }
    factory { MarkNotificationsAsReadUseCase(get()) }
    viewModel { NotificationViewModel(get(), get()) }
}
