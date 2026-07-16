package org.sharad.velvetinvestment.data.remote.repository

import com.mmk.kmpnotifier.notification.NotifierManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.clearAuthTokens
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.mapper.toLoginDto
import org.sharad.velvetinvestment.data.remote.model.notifications.NotificationResponseDto
import org.sharad.velvetinvestment.data.remote.model.notifications.UnreadStatusResponseDto
import org.sharad.velvetinvestment.domain.models.notifications.NotificationDomain
import org.sharad.velvetinvestment.data.remote.model.TradingAccountSubmissionDto.TradingAccountResultDto
import org.sharad.velvetinvestment.data.remote.model.auth.sendotp.SendOtpDto
import org.sharad.velvetinvestment.data.remote.model.auth.tokens.RefreshTokenBody
import org.sharad.velvetinvestment.data.remote.model.auth.tokens.RefreshTokenDto
import org.sharad.velvetinvestment.data.remote.model.auth.verifyotp.VerifyOtpBodyDto
import org.sharad.velvetinvestment.data.remote.model.auth.verifyotp.VerifyOtpDto
import org.sharad.velvetinvestment.data.remote.model.onboarding.OnBoardingBodyDto
import org.sharad.velvetinvestment.data.remote.model.panverification.PANVerifyDto
import org.sharad.velvetinvestment.data.remote.model.tradingaccount.prefilled.TradingAccountPrefilledResponseDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.AssetUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.FinanceUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.GoalsUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.InsuranceUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.LoanUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.ProfileUpdateDto
import org.sharad.velvetinvestment.data.remote.model.useedata.UserDataDto
import org.sharad.velvetinvestment.domain.models.auth.LoginDomain
import org.sharad.velvetinvestment.domain.models.tradingaccount.TradingAccountFormDomain
import org.sharad.velvetinvestment.domain.models.user.PANVerifyDomain
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.Log
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
                        otp = otp,
                        fcm_token = NotifierManager
                            .getPushNotifier()
                            .getToken() ?: ""
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
                authPrefs.setPhoneNumber(dto.user.phone_no)

                NetworkResponse.Success(
                    response.data.toLoginDto()
                )
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

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

    override suspend fun getTradingAccountPrefilledData(): NetworkResponse<org.sharad.velvetinvestment.domain.models.tradingaccount.prefilled.TradingAccountPrefilledDomain, ErrorDomain> {
        val response = safeRequest<TradingAccountPrefilledResponseDto> {
            client.get(getUrl("/kyc/get-form-data"))
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun verifyPAN(pan: String): NetworkResponse<PANVerifyDomain, ErrorDomain> {
        val response= safeRequest<PANVerifyDto> {
            client.get(
                getUrl("/kyc/pan-verify")
            ) {
                parameter("pan_number", pan)
            }
        }

        when(response){
            is NetworkResponse.Error -> {
                return NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success-> {
                return NetworkResponse.Success(response.data.toDomain())
            }
        }

    }

    override suspend fun submitTradingAccountForm(data: TradingAccountFormDomain): NetworkResponse<String, ErrorDomain> {
        val response= safeRequest<TradingAccountResultDto> {
            client.post(
                getUrl("/kyc/trading-account")
            ) {
                setBody(data)
            }
        }

        when(response){
            is NetworkResponse.Error -> {
                return NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success-> {
                return NetworkResponse.Success(response.data.data.short_url)
            }
        }
    }

    override suspend fun tradingAccountConfirmation(
        taxStatus: String,
        holdingNature: String,
        jointHolderName1: String,
        jointHolderName2: String,
        guardianName: String,
        isMinor: Boolean
    ): NetworkResponse<Unit, ErrorDomain> {

        val body = buildMap<String, String> {
            put("tax_status", taxStatus)
            put("holding_nature", holdingNature)

            when {
                isMinor -> {
                    put("guardian_name", guardianName)
                }

                holdingNature == "JO" -> {
                    put("jh1_name", jointHolderName1)

                    if (jointHolderName2.isNotBlank()) {
                        put("jh2_name", jointHolderName2)
                    }
                }
            }
        }

        return safeUnitRequest {
            client.post(
                getUrl("/kyc/trading-confirmation")
            ) {
                setBody(body)
            }
        }
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

    override suspend fun updateGoals(
        data: GoalsUpdateDto
    ): NetworkResponse<Unit, ErrorDomain> {
        return postPartial(data)
    }

    override suspend fun getNotifications(): NetworkResponse<List<NotificationDomain>, ErrorDomain> {
        val response = safeRequest<NotificationResponseDto> {
            client.get(getUrl("/user/notifications"))
        }

        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun getUnreadStatus(): NetworkResponse<Boolean, ErrorDomain> {
        val response = safeRequest<UnreadStatusResponseDto> {
            client.get(getUrl("/user/notifications/unread-status"))
        }

        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data.has_unread)
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun markNotificationsAsRead(): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.patch(getUrl("/user/notifications/read"))
        }
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