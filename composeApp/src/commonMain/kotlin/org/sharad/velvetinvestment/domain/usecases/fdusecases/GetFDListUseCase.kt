package org.sharad.velvetinvestment.domain.usecases.fdusecases

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
import org.sharad.velvetinvestment.domain.repository.FDRepository
import org.sharad.velvetinvestment.presentation.portfolio.models.FDCardData

class GetFDListUseCase(
    private val repository: FDRepository
) {
    suspend operator fun invoke():
            NetworkResponse<List<FDCardData>, NetworkError> {

        return repository.getFDs()
    }
}
