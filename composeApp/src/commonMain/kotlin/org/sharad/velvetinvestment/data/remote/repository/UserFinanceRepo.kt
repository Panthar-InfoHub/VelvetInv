package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.model.firereport.FireReportDto
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.bearer
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
                bearerAuth(bearer)
            }
        }
    }
}