package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface UserRepository {
    fun loginWithUserNamePassword(userName:String,password:String): NetworkResponse<Boolean, ErrorDomain>
    fun requestOtp(phone:String): NetworkResponse<Boolean, ErrorDomain>
    fun validateOtp(otp:String): NetworkResponse<Boolean, ErrorDomain>
    fun signOut(): NetworkResponse<Boolean, ErrorDomain>
}