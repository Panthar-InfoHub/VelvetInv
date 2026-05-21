package org.sharad.velvetinvestment.domain.usecases.portfolio

import org.sharad.velvetinvestment.domain.models.portfolio.PendingOrderDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetPendingOrdersUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(): NetworkResponse<List<PendingOrderDomain>, ErrorDomain> {
        return repository.getPendingOrders()
    }
}