package org.sharad.velvetinvestment.features.loans.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.features.loans.data.remote.model.UpdateLoanRequest
import org.sharad.velvetinvestment.features.loans.domain.repository.LoanRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest

/**
 * Edit / delete a single loan via `/user-loan/{id}`. Adding a loan goes through the FIRE
 * onboarding loan step instead (see `AddLoanViewModel`).
 */
class LoanRepo(
    private val client: HttpClient
) : LoanRepository {

    override suspend fun deleteSingleLoan(id: String): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.delete(getUrl("/user-loan/$id"))
        }
    }

    override suspend fun updateSingleLoan(
        loanId: String,
        data: UpdateLoanRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.patch(getUrl("/user-loan/$loanId")) {
                setBody(data)
            }
        }
    }
}
