package org.sharad.velvetinvestment.features.login.domain.repository

import org.sharad.velvetinvestment.features.login.domain.model.LoginDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

/**
 * JN's login repository. JN also keeps the notification calls here; in Velvet they stay on the
 * app-wide `domain.repository.UserAuth` until the profile feature is ported.
 */
interface UserAuth {
    suspend fun loginWithNumber(number: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun verifyOTP(number: String, otp: String): NetworkResponse<LoginDomain, ErrorDomain>
}
