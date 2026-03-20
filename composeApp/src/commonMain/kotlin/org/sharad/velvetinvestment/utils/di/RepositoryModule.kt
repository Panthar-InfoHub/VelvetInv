package org.sharad.velvetinvestment.utils.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import org.sharad.velvetinvestment.data.remote.repository.CASRepo
import org.sharad.velvetinvestment.data.remote.repository.UserAuthenticationRepo
import org.sharad.velvetinvestment.data.remote.repository.UserFinanceRepo
import org.sharad.velvetinvestment.data.remote.repository.dummy.DummyFDRepositoryPortFolio
import org.sharad.velvetinvestment.data.remote.repository.dummy.DummyMutualFundRepository
import org.sharad.velvetinvestment.data.remote.repository.dummy.FakeFixedDepositRepository
import org.sharad.velvetinvestment.data.remote.repository.dummy.HomeRepositoryMock
import org.sharad.velvetinvestment.domain.repository.FDRepositoryPortFolio
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.domain.repository.HomeRepository
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.getHttpClient
import org.sharad.velvetinvestment.utils.storage.AuthPrefs

val repositoryModule = module {

    single { AuthPrefs(get()) }
    single<HttpClient> { getHttpClient(get()) }
    single<HomeRepository> { HomeRepositoryMock() }
    single<MutualFundRepository> {
        DummyMutualFundRepository()
    }
    single { CASRepo(get()) }

    single<FDRepositoryPortFolio> {
        DummyFDRepositoryPortFolio()
    }

    single<UserFinance> { UserFinanceRepo(get()) }
    single<FixedDepositRepository> { FakeFixedDepositRepository() }

    single<UserAuth> { UserAuthenticationRepo(get(), get(), get()) }
}