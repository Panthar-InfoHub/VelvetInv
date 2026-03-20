package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.data.remote.mapper.toLoginDto
import org.sharad.velvetinvestment.data.remote.model.auth.sendotp.SendOtpDto
import org.sharad.velvetinvestment.data.remote.model.auth.verifyotp.VerifyOtpBodyDto
import org.sharad.velvetinvestment.data.remote.model.auth.verifyotp.VerifyOtpDto
import org.sharad.velvetinvestment.data.remote.model.onboarding.OnBoardingBodyDto
import org.sharad.velvetinvestment.domain.models.auth.LoginDomain
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfoRetriever
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.utils.networking.safeUnitRequest
import org.sharad.velvetinvestment.utils.storage.AuthPrefs

class UserAuthenticationRepo(
    private val client: HttpClient,
    private val authPrefs: AuthPrefs,
    private val deviceInfoRetriever: DeviceInfoRetriever
): UserAuth {
    override suspend fun loginWithNumber(number: String): NetworkResponse<Unit, ErrorDomain> {
        val deviceInfo= deviceInfoRetriever.getDeviceInfo()
        val response= safeRequest<SendOtpDto> {
            client.post(getUrl("/auth/req-otp")) {

                parameter("dtyp", deviceInfo.deviceType)
                parameter("dver", deviceInfo.deviceVersion)
                parameter("dbn", deviceInfo.deviceBuildNumber.take(6))
                parameter("did", deviceInfo.deviceId)
                parameter("mob", number)
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    Unit
                )
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun verifyOTP(
        number: String,
        otp: String
    ): NetworkResponse<LoginDomain, ErrorDomain> {

        val deviceInfo = deviceInfoRetriever.getDeviceInfo()

        val response = safeRequest<VerifyOtpDto> {
            client.post(getUrl("/auth/validate-otp")) {

                parameter("dtyp", deviceInfo.deviceType)
                parameter("dver", deviceInfo.deviceVersion)
                parameter("dbn", deviceInfo.deviceBuildNumber)
                parameter("did", deviceInfo.deviceId)

                setBody(
                    VerifyOtpBodyDto(
                        mob = number,
                        otp = otp
                    )
                )
            }
        }

        return when (response) {

            is NetworkResponse.Success -> {

                val dto = response.data.data

                authPrefs.setBearerToken(dto.token)
                authPrefs.setRefreshToken(dto.refresh_token)
                authPrefs.setUserId(dto.user.user_id)
                authPrefs.setOnboardingCompleted(dto.user.metadata.is_onboarding_completed)
                authPrefs.setOnboardingStep(dto.user.metadata.onboarding_stage)
                authPrefs.setLoggedIn(true)
                NetworkResponse.Success(
                    response.data.toLoginDto()
                )
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun loginWithPassword(
        userId: String,
        password: String,
    ): NetworkResponse<Unit, ErrorDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut(): NetworkResponse<Unit, ErrorDomain> {
        authPrefs.clearAuth()
        return NetworkResponse.Success(Unit)
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
}