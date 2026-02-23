package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository

class GetMutualFundTopPicksUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke():
            NetworkResponse<List<MutualFundTopPicksDomain>, NetworkError> {
        return repository.getMutualFundTopPicks()
    }
}
