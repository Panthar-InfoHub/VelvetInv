package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.domain.CartCountController
import org.sharad.velvetinvestment.features.cart.domain.models.UserCartDomain
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class GetUserCartUseCase(
    private val repository: CartRepository
) {

    /** Fetches the cart, and on success refreshes [CartCountController] for every screen showing it. */
    suspend operator fun invoke(): NetworkResponse<UserCartDomain, ErrorDomain> {
        val response = repository.getCart()
        if (response is NetworkResponse.Success) CartCountController.update(response.data)
        return response
    }
}
