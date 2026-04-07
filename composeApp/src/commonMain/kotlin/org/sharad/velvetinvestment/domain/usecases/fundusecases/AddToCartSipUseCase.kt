package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.data.remote.model.cartaddsip.AddCartSipRequest
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class AddToCartSipUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        request: AddCartSipRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.addToCartSipFund(request)
    }
}