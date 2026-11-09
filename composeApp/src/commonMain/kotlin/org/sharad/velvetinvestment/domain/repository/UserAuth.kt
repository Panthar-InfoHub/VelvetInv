package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.features.login.data.models.auth.tokens.RefreshTokenDto
import org.sharad.velvetinvestment.data.remote.model.onboarding.OnBoardingBodyDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.AssetUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.FinanceUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.InsuranceUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.LoanUpdateDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.ProfileUpdateDto
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserDataDto

interface UserAuth {
//    suspend fun loginWithPassword(userId: String, password: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun signOut(): NetworkResponse<Unit, ErrorDomain>

    suspend fun refreshToken(refreshToken: String): NetworkResponse<RefreshTokenDto, ErrorDomain>
    suspend fun onBoardUser(data: OnBoardingBodyDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateAssets(data: AssetUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateFinance(data: FinanceUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateInsurance(data: InsuranceUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateLoans(data: LoanUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateProfile(data: ProfileUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun getUserData(): NetworkResponse<UserDataDto, ErrorDomain>
}