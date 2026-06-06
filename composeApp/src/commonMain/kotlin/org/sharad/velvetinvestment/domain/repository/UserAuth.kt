package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.data.remote.model.auth.tokens.RefreshTokenDto
import org.sharad.velvetinvestment.data.remote.model.onboarding.OnBoardingBodyDto
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
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface UserAuth {
    suspend fun loginWithNumber(number: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun verifyOTP(number: String,otp:String): NetworkResponse<LoginDomain, ErrorDomain>
//    suspend fun loginWithPassword(userId: String, password: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun signOut(): NetworkResponse<Unit, ErrorDomain>

    suspend fun refreshToken(refreshToken: String): NetworkResponse<RefreshTokenDto, ErrorDomain>
    suspend fun onBoardUser(data: OnBoardingBodyDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateAssets(data: AssetUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateFinance(data: FinanceUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateInsurance(data: InsuranceUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateLoans(data: LoanUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateProfile(data: ProfileUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateGoals(data: GoalsUpdateDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun getUserData(): NetworkResponse<UserDataDto, ErrorDomain>

    suspend fun getTradingAccountPrefilledData(): NetworkResponse<org.sharad.velvetinvestment.domain.models.tradingaccount.prefilled.TradingAccountPrefilledDomain, ErrorDomain>

    suspend fun  verifyPAN(pan: String): NetworkResponse<PANVerifyDomain, ErrorDomain>

    suspend fun submitTradingAccountForm(data: TradingAccountFormDomain) : NetworkResponse<String, ErrorDomain>
    suspend fun tradingAccountConfirmation(
        taxStatus: String,
        holdingNature: String,
        jointHolderName1: String,
        jointHolderName2: String,
        guardianName: String,
        isMinor: Boolean
    ): NetworkResponse<Unit, ErrorDomain>
}