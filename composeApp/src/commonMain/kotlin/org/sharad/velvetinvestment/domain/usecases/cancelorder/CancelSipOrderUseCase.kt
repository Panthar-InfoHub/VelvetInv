package org.sharad.velvetinvestment.domain.usecases.cancelorder

import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class CancelSipOrderUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(xsipRegNo: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.cancelSipOrder(xsipRegNo)
    }
}
