package org.sharad.velvetinvestment.features.orders.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.sharad.velvetinvestment.features.orders.data.repository.OrdersRepo
import org.sharad.velvetinvestment.features.orders.domain.repository.OrdersRepository
import org.sharad.velvetinvestment.features.orders.domain.usecase.GetOrdersUseCase
import org.sharad.velvetinvestment.features.orders.ui.viewmodel.MyOrdersViewModel

val ordersFeatureModule = module {
    single<OrdersRepository> { OrdersRepo(get()) }
    factory { GetOrdersUseCase(get()) }
    viewModel { MyOrdersViewModel(get()) }
}
