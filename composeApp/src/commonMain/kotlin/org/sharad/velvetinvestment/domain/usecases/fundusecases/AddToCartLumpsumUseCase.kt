package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class AddToCartLumpsumUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        id: String,
        amount: Long
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.addToCartLumSumFund(
            id = id,
            amount = amount
        )
    }
}