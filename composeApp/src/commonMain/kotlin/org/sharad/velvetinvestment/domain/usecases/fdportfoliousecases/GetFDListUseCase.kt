package org.sharad.velvetinvestment.domain.usecases.fdportfoliousecases

import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.repository.FDRepositoryPortFolio
import org.sharad.velvetinvestment.presentation.portfolio.models.FDCardPortfolioData

class GetFDListUseCase(
    private val repository: FDRepositoryPortFolio
) {
    suspend operator fun invoke():
            NetworkResponse<List<FDCardPortfolioData>, NetworkError> {

        return repository.getFDs()
    }
}
