package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.data.remote.model.updateuserdata.LoanUpdateDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UpdateLoansUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(data: LoanUpdateDto): NetworkResponse<Unit, ErrorDomain> {
        return userAuth.updateLoans(data)
    }
}