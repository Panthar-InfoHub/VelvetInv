package org.sharad.velvetinvestment.domain.usecases.loan

import org.sharad.velvetinvestment.data.remote.model.loan.UpdateLoanRequest
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UpdateSingleLoanUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(
        loanId: String,
        data: UpdateLoanRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.updateSingleLoan(loanId, data)
    }
}
