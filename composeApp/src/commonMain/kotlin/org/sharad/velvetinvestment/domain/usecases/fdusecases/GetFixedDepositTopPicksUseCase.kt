package org.sharad.velvetinvestment.domain.usecases.fdusecases

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.explore.FixedDepositTopPicksDomain
import org.sharad.velvetinvestment.domain.repository.FDRepository

class GetFixedDepositTopPicksUseCase(
    private val repository: FDRepository
) {
    suspend operator fun invoke():
            NetworkResponse<List<FixedDepositTopPicksDomain>, NetworkError> {
        return repository.getFixedDepositTopPicks()
    }
}
