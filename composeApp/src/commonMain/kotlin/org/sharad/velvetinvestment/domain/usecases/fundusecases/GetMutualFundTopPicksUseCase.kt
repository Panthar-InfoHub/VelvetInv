package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetMutualFundTopPicksUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke():
            NetworkResponse<List<MutualFundTopPicksDomain>, ErrorDomain> {
        return repository.getMutualFundTopPicks()
    }
}
