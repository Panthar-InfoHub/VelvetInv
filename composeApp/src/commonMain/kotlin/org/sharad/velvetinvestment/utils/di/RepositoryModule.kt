package org.sharad.velvetinvestment.utils.di

import org.koin.dsl.module
import org.sharad.velvetinvestment.data.remote.repository.DummyFDRepository
import org.sharad.velvetinvestment.data.remote.repository.DummyMutualFundRepository
import org.sharad.velvetinvestment.data.remote.repository.HomeRepositoryMock
import org.sharad.velvetinvestment.domain.repository.FDRepository
import org.sharad.velvetinvestment.domain.repository.HomeRepository
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository

val repositoryModule = module {
    single<HomeRepository> { HomeRepositoryMock() }
    single<MutualFundRepository> {
        DummyMutualFundRepository()
    }

    single<FDRepository> {
        DummyFDRepository()
    }
}