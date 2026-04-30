package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.data.remote.model.updateuserdata.InsuranceUpdateDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UpdateInsuranceUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(data: InsuranceUpdateDto): NetworkResponse<Unit, ErrorDomain> {
        return userAuth.updateInsurance(data)
    }
}