package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.portfolio.domain.models.ActiveSipDomain
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo

/** The running SIPs behind the portfolio's Active SIP tab: `GET /mf/purchase-plan?state=ACTIVE`. */
class GetActiveSipsUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(): NetworkResponse<ActiveSipDomain, ErrorDomain> =
        repository.getActiveSips()
}
