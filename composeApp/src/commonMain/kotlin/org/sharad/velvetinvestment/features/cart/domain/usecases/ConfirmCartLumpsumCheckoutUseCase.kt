package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.domain.models.LumpsumCheckoutPaymentDomain
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class ConfirmCartLumpsumCheckoutUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        batchId: String,
        otp: String,
        paymentPostbackUrl: String
    ): NetworkResponse<LumpsumCheckoutPaymentDomain, ErrorDomain> {
        return repository.confirmLumpsumCheckout(
            batchId = batchId,
            otp = otp,
            paymentPostbackUrl = paymentPostbackUrl
        )
    }
}
