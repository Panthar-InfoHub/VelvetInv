package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetFDRedirectUrlUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(id: String, event: String): NetworkResponse<String, ErrorDomain> {
        return repository.getFDRedirectUrl(id, event)
    }
}
