package org.sharad.velvetinvestment.domain.usecases.loan

import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.loan.LoanDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetLoansUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(
        page: Int,
        limit: Int
    ): NetworkResponse<PaginatedData<LoanDomain>, ErrorDomain> {
        return repository.getLoans(page, limit)
    }
}
