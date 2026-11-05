package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.data.remote.model.bundlecart.AddBundleSipRequest
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class AddBundleToCartSipUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        request: AddBundleSipRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.addBundleToCartSip(request)
    }
}
