package org.sharad.velvetinvestment.domain.usecases.fdportfoliousecases

import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.explore.FixedDepositTopPicksDomain
import org.sharad.velvetinvestment.domain.repository.FDRepositoryPortFolio

class GetFixedDepositTopPicksUseCase(
    private val repository: FDRepositoryPortFolio
) {
    suspend operator fun invoke():
            NetworkResponse<List<FixedDepositTopPicksDomain>, NetworkError> {
        return repository.getFixedDepositTopPicks()
    }
}
