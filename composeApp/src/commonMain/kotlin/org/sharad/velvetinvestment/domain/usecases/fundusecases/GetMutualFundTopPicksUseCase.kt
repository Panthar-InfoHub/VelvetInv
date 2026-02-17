package org.sharad.velvetinvestment.domain.usecases.fundusecases

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
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
