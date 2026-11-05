package org.sharad.velvetinvestment.features.mutualfund.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.mutualfund.domain.models.MutualFundDetailsDomain
import org.sharad.velvetinvestment.features.mutualfund.domain.repository.MutualFundRepository

class GetMutualFundDetailsUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        id: String
    ): NetworkResponse<MutualFundDetailsDomain, ErrorDomain> {
        return repository.getMutualFundDetails(id)
    }
}
