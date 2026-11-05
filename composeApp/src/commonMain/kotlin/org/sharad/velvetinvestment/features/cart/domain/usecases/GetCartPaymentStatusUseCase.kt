package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.domain.models.CartPaymentStatusDomain
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class GetCartPaymentStatusUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(paymentId: String): NetworkResponse<CartPaymentStatusDomain, ErrorDomain> {
        return repository.getPaymentStatus(paymentId)
    }
}
