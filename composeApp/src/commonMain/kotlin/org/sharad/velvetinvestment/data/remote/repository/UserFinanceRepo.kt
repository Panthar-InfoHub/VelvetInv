package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.model.casreport.RedirectBody
import org.sharad.velvetinvestment.data.remote.model.fdportfoliobyid.FDPortFolioById
import org.sharad.velvetinvestment.data.remote.model.fdredirect.FDRedirectDto
import org.sharad.velvetinvestment.data.remote.model.firereport.FireReportDto
import org.sharad.velvetinvestment.data.remote.model.pendingorders.PendingOrdersDto
import org.sharad.velvetinvestment.data.remote.model.portfolio.FolioFundsDto
import org.sharad.velvetinvestment.data.remote.model.portfolio.UserPortFolioDto
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FolioFundDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositTransactionDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDomain
import org.sharad.velvetinvestment.data.remote.model.report.ReportExportDto
import org.sharad.velvetinvestment.domain.models.portfolio.PendingOrderDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.ErrorType
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest

class UserFinanceRepo(
    private val client: HttpClient
): UserFinance {

    override suspend fun getFireReport(): NetworkResponse<FireReportDomain, ErrorDomain> {
        val response= safeRequest<FireReportDto> {
            client.get(getUrl("/fire-report")) {
                parameter("include_emi", true)
                parameter("projection_years",20)
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun getFirePdf(): NetworkResponse<ByteArray, ErrorDomain> {
        return safeRequest<ByteArray>{
            client.get(getUrl("/fire-report/pdf")) {
            }
        }
    }

    override suspend fun getPortfolio(): NetworkResponse<PortfolioDomain, ErrorDomain> {
        val response= safeRequest<UserPortFolioDto> {
            client.get(getUrl("/user/portfolio"))
        }

        return when(response){
            is NetworkResponse.Error->{
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success ->{
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
        }
    }

    override suspend fun getFDPortfolioById(id: String): NetworkResponse<FixedDepositTransactionDomain, ErrorDomain> {
        val response = safeRequest<FDPortFolioById> {
            client.get(getUrl("/fd/transactions/$id"))
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
        }
    }

    override suspend fun getFDRedirectUrl(
        id: String,
        event: String
    ): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<FDRedirectDto> {
            client.post(
                getUrl("/fd/redirect-url")
            ) {
                setBody(
                    RedirectBody(
                        fd_trans_id = id,
                        event = event
                    )
                )
            }
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.data.redirectionUrl)
            }
        }
    }

    override suspend fun exportReport(
        type: String,
        year: Int?,
        folio: String?,
        expand: Int?
    ): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<ReportExportDto> {
            client.get(getUrl("/report")) {
                parameter("type", type)
                year?.let { parameter("year", it) }
                folio?.let { parameter("folio", it) }
                expand?.let { parameter("expand", it) }
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                val url = response.data.data.result
                if (url==null){
                     NetworkResponse.Error(ErrorDomain(
                        code = -1,
                        message = "No Data Found",
                        type = ErrorType.UNKNOWN
                    ))
                }
                else
                NetworkResponse.Success(url)
            }
        }
    }

    override suspend fun getPendingOrders(): NetworkResponse<List<PendingOrderDomain>, ErrorDomain> {
        val response = safeRequest<PendingOrdersDto> {
            client.get(getUrl("/user/pending-orders"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.data?.pending_orders?.map { it.toDomain() } ?: emptyList()
                )
            }
        }
    }

    override suspend fun getFolioFunds(folioId: String): NetworkResponse<List<FolioFundDomain>, ErrorDomain> {
        val response = safeRequest<FolioFundsDto> {
            client.get(getUrl("/user/portfolio/$folioId"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.data.map { it.toDomain() }
                )
            }
        }
    }

}