package org.sharad.velvetinvestment.domain.usecases.fundusecases

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData

class GetMutualFundDashboardUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke():
            NetworkResponse<MutualFundDashBoardData, NetworkError> {

        return repository.getDashboard()
    }
}
