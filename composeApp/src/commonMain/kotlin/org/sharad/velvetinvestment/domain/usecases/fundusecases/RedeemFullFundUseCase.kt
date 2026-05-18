package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class RedeemFullFundUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        data: FullRedemptionRequestDto
    ): NetworkResponse<String, ErrorDomain> {
        return repository.redeemFullFund(data)
    }
}
