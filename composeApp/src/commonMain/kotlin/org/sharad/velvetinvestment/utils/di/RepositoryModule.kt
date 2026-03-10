package org.sharad.velvetinvestment.utils.di

import org.koin.dsl.module
import org.sharad.velvetinvestment.data.remote.repository.DummyFDRepositoryPortFolio
import org.sharad.velvetinvestment.data.remote.repository.DummyMutualFundRepository
import org.sharad.velvetinvestment.data.remote.repository.DummyUserFinance
import org.sharad.velvetinvestment.data.remote.repository.FakeFixedDepositRepository
import org.sharad.velvetinvestment.data.remote.repository.HomeRepositoryMock
import org.sharad.velvetinvestment.domain.repository.FDRepositoryPortFolio
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.domain.repository.HomeRepository
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.domain.repository.UserFinance

val repositoryModule = module {

    single<HomeRepository> { HomeRepositoryMock() }
    single<MutualFundRepository> {
        DummyMutualFundRepository()
    }

    single<FDRepositoryPortFolio> {
        DummyFDRepositoryPortFolio()
    }

    single<UserFinance> { DummyUserFinance() }
    single<FixedDepositRepository> { FakeFixedDepositRepository() }
}