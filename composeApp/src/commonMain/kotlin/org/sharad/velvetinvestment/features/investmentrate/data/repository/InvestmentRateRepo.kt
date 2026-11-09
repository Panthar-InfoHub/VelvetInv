package org.sharad.velvetinvestment.features.investmentrate.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.sharad.velvetinvestment.features.investmentrate.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.features.investmentrate.data.remote.model.InvestmentRateDto
import org.sharad.velvetinvestment.features.investmentrate.domain.model.InvestmentRateDomain
import org.sharad.velvetinvestment.features.investmentrate.domain.repository.InvestmentRateRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest

class InvestmentRateRepo(
    private val client: HttpClient
) : InvestmentRateRepository {

    override suspend fun getInvestmentRateData(): NetworkResponse<InvestmentRateDomain, ErrorDomain> {
        val response = safeRequest<InvestmentRateDto> {
            client.get(getUrl("/user/investment-rate"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
        }
    }
}
