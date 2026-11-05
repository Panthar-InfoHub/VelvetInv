package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class VerifyCartSipCheckoutOtpUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(batchId: String, otp: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.verifySipCheckoutOtp(batchId = batchId, otp = otp)
    }
}
