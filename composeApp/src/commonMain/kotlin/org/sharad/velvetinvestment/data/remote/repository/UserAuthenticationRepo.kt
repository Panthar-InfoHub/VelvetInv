package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.clearAuthTokens
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.features.login.data.models.auth.tokens.RefreshTokenBody
import org.sharad.velvetinvestment.features.login.data.models.auth.tokens.RefreshTokenDto
import org.sharad.velvetinvestment.data.remote.model.onboarding.OnBoardingBodyDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.AssetUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.FinanceUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.InsuranceUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.LoanUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.ProfileUpdateDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.Log
import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfoRetriever
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.utils.networking.safeUnitRequest
import org.sharad.velvetinvestment.utils.storage.AuthPrefs
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserDataDto

class UserAuthenticationRepo(
    private val client: HttpClient,
    private val authPrefs: AuthPrefs,
    private val deviceInfoRetriever: DeviceInfoRetriever
): UserAuth {
//    override suspend fun loginWithPassword(
//        userId: String,
//        password: String,
//    ): NetworkResponse<Unit, ErrorDomain> {
//        //TODO("Not yet implemented")
//    }

    override suspend fun signOut(): NetworkResponse<Unit, ErrorDomain> {
        Log("Auth Tokens", client.authProviders.toString())
        authPrefs.clearAuth()
        client.clearAuthTokens()
        Log("Auth Tokens", client.authProviders.toString())
        return NetworkResponse.Success(Unit)
    }

    override suspend fun refreshToken(refreshToken: String): NetworkResponse<RefreshTokenDto, ErrorDomain> {
       return safeRequest<RefreshTokenDto> {
            client.post(getUrl("/auth/refresh-token")) {
                setBody(RefreshTokenBody(refreshToken))
            }
        }

    }

    override suspend fun onBoardUser(data: OnBoardingBodyDto): NetworkResponse<Unit, ErrorDomain> {
        val response= safeUnitRequest {
            client.post(
                getUrl("/onboarding/complete-onboarding")
            ) {
                setBody(data)
            }
        }

        when(response){
            is NetworkResponse.Error -> {
                return NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success ->{
                authPrefs.setOnboardingCompleted(true)
                authPrefs.setLoggedIn(true)
               return response
            }
        }
    }

    override suspend fun getUserData(): NetworkResponse<UserDataDto, ErrorDomain> {
        val response= safeRequest<UserDataDto> {
            client.get(
                getUrl("/user")
            )
        }
        return  response
    }

    override suspend fun updateAssets(
        data: AssetUpdateDto
    ): NetworkResponse<Unit, ErrorDomain> {
        return postPartial(data)
    }

    override suspend fun updateFinance(
        data: FinanceUpdateDto
    ): NetworkResponse<Unit, ErrorDomain> {
        return postPartial(data)
    }

    override suspend fun updateInsurance(
        data: InsuranceUpdateDto
    ): NetworkResponse<Unit, ErrorDomain> {
        return postPartial(data)
    }

    override suspend fun updateLoans(
        data: LoanUpdateDto
    ): NetworkResponse<Unit, ErrorDomain> {
        return postPartial(data)
    }

    override suspend fun updateProfile(
        data: ProfileUpdateDto
    ): NetworkResponse<Unit, ErrorDomain> {
        return postPartial(data)
    }

    private suspend inline fun <reified T> postPartial(
        body: T
    ): NetworkResponse<Unit, ErrorDomain> {

        val response = safeUnitRequest {
            client.post(getUrl("/onboarding/complete-onboarding")) {
                setBody<T>(body)
            }
        }

        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> response
        }
    }
}