package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.data.remote.model.updateuserdata.AssetUpdateDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UpdateAssetsUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(data: AssetUpdateDto): NetworkResponse<Unit, ErrorDomain> {
        return userAuth.updateAssets(data)
    }
}