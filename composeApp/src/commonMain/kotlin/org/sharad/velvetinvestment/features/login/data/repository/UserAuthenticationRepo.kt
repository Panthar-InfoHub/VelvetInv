package org.sharad.velvetinvestment.features.login.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.features.login.data.mapper.toLoginDomain
import org.sharad.velvetinvestment.features.login.data.models.auth.sendotp.SendOtpDto
import org.sharad.velvetinvestment.features.login.data.models.auth.verifyotp.VerifyOtpBodyDto
import org.sharad.velvetinvestment.features.login.data.models.auth.verifyotp.VerifyOtpDto
import org.sharad.velvetinvestment.features.login.domain.model.LoginDomain
import org.sharad.velvetinvestment.features.login.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.deviceinfoprovider.DeviceInfoRetriever
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.utils.notification.PushNotificationManager
import org.sharad.velvetinvestment.utils.storage.AuthPrefs

class UserAuthenticationRepo(
    private val client: HttpClient,
    private val authPrefs: AuthPrefs,
    private val deviceInfoRetriever: DeviceInfoRetriever,
    private val pushNotificationManager: PushNotificationManager
) : UserAuth {

    override suspend fun loginWithNumber(number: String): NetworkResponse<Unit, ErrorDomain> {
        val deviceInfo = deviceInfoRetriever.getDeviceInfo()
        val response = safeRequest<SendOtpDto> {
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
                NetworkResponse.Success(Unit)
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
        // The Firebase Installation ID, which the backend registers for push (JN).
        val token = pushNotificationManager.getToken()

        val response = safeRequest<VerifyOtpDto> {
            client.post(getUrl("/auth/validate-otp")) {
                parameter("dtyp", deviceInfo.deviceType)
                parameter("dver", deviceInfo.deviceVersion)
                parameter("dbn", deviceInfo.deviceBuildNumber)
                parameter("did", deviceInfo.deviceId)

                setBody(
                    VerifyOtpBodyDto(
                        mob = number,
                        otp = otp,
                        fcm_token = token
                    )
                )
            }
        }

        return when (response) {
            is NetworkResponse.Success -> {
                val dto = response.data.data
                val login = response.data.toLoginDomain()

                authPrefs.setBearerToken(dto.token)
                authPrefs.setRefreshToken(dto.refresh_token)
                authPrefs.setLoggedIn(true)
                authPrefs.setUserId(dto.user.user_id)
                authPrefs.setPhoneNumber(dto.user.phone_no)

                // Locally "onboarded" means the server lets the user into the main app, which
                // takes over chasing whatever is still outstanding.
                authPrefs.setOnboardingCompleted(login.canEnterMainApp)
                NetworkResponse.Success(login)
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }
}
