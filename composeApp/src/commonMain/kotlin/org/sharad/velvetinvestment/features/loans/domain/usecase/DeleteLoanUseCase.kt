package org.sharad.velvetinvestment.features.loans.domain.usecase

import org.sharad.velvetinvestment.features.loans.domain.repository.LoanRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class DeleteLoanUseCase(
    private val repository: LoanRepository
) {
    suspend operator fun invoke(id: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.deleteSingleLoan(id)
    }
}
