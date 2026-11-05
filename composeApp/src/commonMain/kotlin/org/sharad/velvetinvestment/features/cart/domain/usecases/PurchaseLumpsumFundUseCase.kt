package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class PurchaseLumpsumFundUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(): NetworkResponse<String, ErrorDomain> {
        return repository.purchaseLumpSum()
    }
}