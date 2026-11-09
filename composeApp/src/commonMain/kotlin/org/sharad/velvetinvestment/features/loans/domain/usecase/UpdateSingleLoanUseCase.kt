package org.sharad.velvetinvestment.features.loans.domain.usecase

import org.sharad.velvetinvestment.features.loans.data.remote.model.UpdateLoanRequest
import org.sharad.velvetinvestment.features.loans.domain.repository.LoanRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UpdateSingleLoanUseCase(
    private val repository: LoanRepository
) {
    suspend operator fun invoke(
        loanId: String,
        data: UpdateLoanRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.updateSingleLoan(loanId, data)
    }
}
