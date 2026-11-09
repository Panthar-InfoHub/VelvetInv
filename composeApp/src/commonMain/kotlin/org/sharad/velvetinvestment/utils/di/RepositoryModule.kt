package org.sharad.velvetinvestment.utils.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import org.sharad.velvetinvestment.data.remote.repository.CASRepo
import org.sharad.velvetinvestment.data.remote.repository.FixedDepositRepo
import org.sharad.velvetinvestment.data.remote.repository.MFKYCRepo
import org.sharad.velvetinvestment.data.remote.repository.UserAuthenticationRepo
import org.sharad.velvetinvestment.data.remote.repository.UserFinanceRepo
import org.sharad.velvetinvestment.data.remote.repository.dummy.HomeRepositoryMock
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.domain.repository.HomeRepository
import org.sharad.velvetinvestment.domain.repository.MFKYCRepository
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.shared.data.remote.repository.UserDataRepoImpl
import org.sharad.velvetinvestment.shared.domain.repository.UserDataRepo
import org.sharad.velvetinvestment.utils.networking.getHttpClient
import org.sharad.velvetinvestment.utils.storage.AuthPrefs

val repositoryModule = module {

    single { AuthPrefs(get()) }
    single<HttpClient> { getHttpClient(get()) }
    single<HomeRepository> { HomeRepositoryMock() }
    single { CASRepo(get()) }


    single<UserFinance> { UserFinanceRepo(get()) }
    single<FixedDepositRepository> { FixedDepositRepo(get()) }

    single<UserAuth> { UserAuthenticationRepo(get(), get(), get()) }
    single<UserDataRepo> { UserDataRepoImpl(get()) }
    single<MFKYCRepository> { MFKYCRepo(get()) }
}