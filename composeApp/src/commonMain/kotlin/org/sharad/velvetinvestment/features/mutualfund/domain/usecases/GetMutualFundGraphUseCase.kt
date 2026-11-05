package org.sharad.velvetinvestment.features.mutualfund.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.mutualfund.domain.models.MutualFundGraphDomain
import org.sharad.velvetinvestment.features.mutualfund.domain.repository.MutualFundRepository

class GetMutualFundGraphUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        id: String,
        selectedYear: String
    ): NetworkResponse<MutualFundGraphDomain, ErrorDomain> {
        return repository.getMutualFundGraph(id,selectedYear)
    }
}
