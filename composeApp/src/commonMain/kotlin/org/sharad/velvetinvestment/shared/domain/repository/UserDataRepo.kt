package org.sharad.velvetinvestment.shared.domain.repository


import org.sharad.velvetinvestment.shared.domain.models.UserDataDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface UserDataRepo {
    suspend fun getUserData(): NetworkResponse<UserDataDomain, ErrorDomain>
}