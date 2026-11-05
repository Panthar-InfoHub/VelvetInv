package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class AddToCartLumpsumUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        id: String,
        amount: Long
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.addToCartLumpsum(
            mfProductId = id,
            amount = amount
        )
    }
}
