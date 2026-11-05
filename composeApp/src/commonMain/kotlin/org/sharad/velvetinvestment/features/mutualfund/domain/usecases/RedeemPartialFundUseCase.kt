package org.sharad.velvetinvestment.features.mutualfund.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.mutualfund.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.sharad.velvetinvestment.features.mutualfund.domain.repository.MutualFundRepository

class RedeemPartialFundUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        data: PartialRedemptionRequestDto
    ): NetworkResponse<String, ErrorDomain> {
        return repository.redeemPartialFund(data)
    }
}
