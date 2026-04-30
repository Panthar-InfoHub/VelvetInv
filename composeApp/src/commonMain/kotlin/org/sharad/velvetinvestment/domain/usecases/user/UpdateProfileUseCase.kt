package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.data.remote.model.updateuserdata.ProfileUpdateDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UpdateProfileUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(data: ProfileUpdateDto): NetworkResponse<Unit, ErrorDomain> {
        return userAuth.updateProfile(data)
    }
}