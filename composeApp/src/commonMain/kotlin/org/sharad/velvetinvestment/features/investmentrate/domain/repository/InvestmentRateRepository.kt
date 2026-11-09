package org.sharad.velvetinvestment.features.investmentrate.domain.repository

import org.sharad.velvetinvestment.features.investmentrate.domain.model.InvestmentRateDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface InvestmentRateRepository {
    suspend fun getInvestmentRateData(): NetworkResponse<InvestmentRateDomain, ErrorDomain>
}
