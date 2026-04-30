package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.data.remote.model.updateuserdata.FinanceUpdateDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UpdateFinanceUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(data: FinanceUpdateDto): NetworkResponse<Unit, ErrorDomain> {
        return userAuth.updateFinance(data)
    }
}