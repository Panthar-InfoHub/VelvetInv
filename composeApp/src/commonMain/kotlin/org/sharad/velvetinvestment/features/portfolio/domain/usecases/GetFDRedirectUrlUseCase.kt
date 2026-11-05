package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo

class GetFDRedirectUrlUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(id: String, event: String): NetworkResponse<String, ErrorDomain> {
        return repository.getFDRedirectUrl(id, event)
    }
}
