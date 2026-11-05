package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.data.remote.model.investmore.InvestMoreDto
import org.sharad.velvetinvestment.data.remote.model.loan.UpdateLoanRequest
import org.sharad.velvetinvestment.data.remote.model.onboarding.Loan
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.domain.models.loan.LoanDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FolioFundDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PendingOrderDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositTransactionDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDomain
import org.sharad.velvetinvestment.domain.models.user.InvestmentRateDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface UserFinance {

    suspend fun getFireReport():
            NetworkResponse<FireReportDomain, ErrorDomain>

    suspend fun getFirePdf():
            NetworkResponse<ByteArray, ErrorDomain>

    suspend fun getPortfolio(): NetworkResponse<PortfolioDomain, ErrorDomain>
    suspend fun getFDPortfolioById(id:String): NetworkResponse<FixedDepositTransactionDomain, ErrorDomain>

    suspend fun getFDRedirectUrl(id:String, event: String): NetworkResponse<String, ErrorDomain>

    suspend fun getInvestmentRateData(): NetworkResponse<InvestmentRateDomain, ErrorDomain>

    suspend fun deleteSingleLoan(id: String): NetworkResponse<Unit, ErrorDomain>

    suspend fun getLoans(page: Int, limit: Int): NetworkResponse<PaginatedData<LoanDomain>, ErrorDomain>

    suspend fun getLoanById(id: String): NetworkResponse<LoanDomain, ErrorDomain>

    suspend fun addSingleLoan(data: Loan): NetworkResponse<Unit, ErrorDomain>

    suspend fun updateSingleLoan(loanId: String, data: UpdateLoanRequest): NetworkResponse<Unit, ErrorDomain>

    suspend fun exportReport(
        type: String,
        year: Int? = null,
        folio: String? = null,
        expand: Int? = null
    ): NetworkResponse<String, ErrorDomain>

    suspend fun getPendingOrders(): NetworkResponse<List<PendingOrderDomain>, ErrorDomain>

    suspend fun getFolioFunds(folioId: String): NetworkResponse<List<FolioFundDomain>, ErrorDomain>

    suspend fun investMoreLumpsum(body: InvestMoreDto): NetworkResponse<String, ErrorDomain>

    suspend fun requestConnection(type: String, message: String): NetworkResponse<Unit, ErrorDomain>
}