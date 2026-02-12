package org.sharad.velvetinvestment.utils.di

import org.koin.dsl.module
import org.sharad.velvetinvestment.data.remote.repository.HomeRepositoryMock
import org.sharad.velvetinvestment.domain.repository.HomeRepository

val repositoryModule = module {
    single<HomeRepository> { HomeRepositoryMock() }
}