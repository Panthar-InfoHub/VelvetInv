package org.sharad.velvetinvestment.domain.usecases.loan

import org.sharad.velvetinvestment.domain.models.loan.LoanDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetLoanByIdUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(id: String): NetworkResponse<LoanDomain, ErrorDomain> {
        return repository.getLoanById(id)
    }
}
