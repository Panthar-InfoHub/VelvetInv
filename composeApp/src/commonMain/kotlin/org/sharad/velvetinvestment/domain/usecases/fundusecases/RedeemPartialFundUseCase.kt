package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class RedeemPartialFundUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        data: PartialRedemptionRequestDto
    ): NetworkResponse<String, ErrorDomain> {
        return repository.redeemPartialFund(data)
    }
}
