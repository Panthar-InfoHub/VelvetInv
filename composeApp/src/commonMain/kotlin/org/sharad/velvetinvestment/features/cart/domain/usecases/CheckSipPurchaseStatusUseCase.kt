package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.features.cart.domain.models.SIPStatus
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class CheckSipPurchaseStatusUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain> {
        return repository.checkSipPurchaseStatus(mandateId)
    }
}
