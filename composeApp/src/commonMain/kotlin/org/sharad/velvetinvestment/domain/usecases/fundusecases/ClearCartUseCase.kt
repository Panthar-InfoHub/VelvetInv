package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class ClearCartUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(): NetworkResponse<Unit, ErrorDomain> {
        return repository.clearCart()
    }
}
