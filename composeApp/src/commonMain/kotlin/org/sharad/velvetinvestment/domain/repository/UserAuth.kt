package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.data.remote.model.onboarding.OnBoardingBodyDto
import org.sharad.velvetinvestment.data.remote.model.useedata.UserDataDto
import org.sharad.velvetinvestment.domain.models.auth.LoginDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface UserAuth {
    suspend fun loginWithNumber(number: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun verifyOTP(number: String,otp:String): NetworkResponse<LoginDomain, ErrorDomain>
    suspend fun loginWithPassword(userId: String, password: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun signOut(): NetworkResponse<Unit, ErrorDomain>
    suspend fun onBoardUser(data: OnBoardingBodyDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun getUserData(): NetworkResponse<UserDataDto, ErrorDomain>
}