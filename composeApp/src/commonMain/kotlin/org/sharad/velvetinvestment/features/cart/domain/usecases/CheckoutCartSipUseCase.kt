package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.domain.models.CartCheckoutDomain
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class CheckoutCartSipUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(mandateId: String): NetworkResponse<CartCheckoutDomain, ErrorDomain> {
        return repository.checkoutSip(mandateId)
    }
}
