package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.data.remote.model.cartaddsip.AddCartSipRequest
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class AddToCartSipUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        request: AddCartSipRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.addToCartSip(request)
    }
}