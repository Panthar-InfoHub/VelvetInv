package org.sharad.velvetinvestment.features.mutualfund.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.mutualfund.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.sharad.velvetinvestment.features.mutualfund.domain.repository.MutualFundRepository

class RedeemFullFundUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        data: FullRedemptionRequestDto
    ): NetworkResponse<String, ErrorDomain> {
        return repository.redeemFullFund(data)
    }
}
