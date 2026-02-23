package org.sharad.velvetinvestment.domain.usecases.fdusecases

import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
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
