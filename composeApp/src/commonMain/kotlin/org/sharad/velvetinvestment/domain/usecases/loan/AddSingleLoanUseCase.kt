package org.sharad.velvetinvestment.domain.usecases.loan

import org.sharad.velvetinvestment.data.remote.model.onboarding.Loan
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class AddSingleLoanUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(data: Loan): NetworkResponse<Unit, ErrorDomain> {
        return repository.addSingleLoan(data)
    }
}
