package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.data.remote.model.bundlecart.AddBundleLumpsumRequest
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class AddBundleToCartLumpsumUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        request: AddBundleLumpsumRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.addBundleToCartLumpsum(request)
    }
}
