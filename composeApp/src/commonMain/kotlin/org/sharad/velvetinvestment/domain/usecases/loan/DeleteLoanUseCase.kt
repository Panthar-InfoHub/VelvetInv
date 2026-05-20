package org.sharad.velvetinvestment.domain.usecases.loan

import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class DeleteLoanUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(id: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.deleteSingleLoan(id)
    }
}
