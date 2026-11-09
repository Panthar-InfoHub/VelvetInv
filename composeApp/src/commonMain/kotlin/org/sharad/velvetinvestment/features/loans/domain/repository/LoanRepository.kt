package org.sharad.velvetinvestment.features.loans.domain.repository

import org.sharad.velvetinvestment.features.loans.data.remote.model.UpdateLoanRequest
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface LoanRepository {
    suspend fun deleteSingleLoan(id: String): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateSingleLoan(loanId: String, data: UpdateLoanRequest): NetworkResponse<Unit, ErrorDomain>
}
