package org.sharad.velvetinvestment.domain.usecases.cancelorder

import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class CancelLumpSumOrderUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(orderId: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.cancelLumpSumOrder(orderId)
    }
}
