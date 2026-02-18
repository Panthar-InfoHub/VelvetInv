package org.sharad.velvetinvestment.domain.usecases.fdusecases

import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.portfolio.FDDetailsDomain
import org.sharad.velvetinvestment.domain.repository.FDRepository

class GetFDDetailsUseCase(
    private val repository: FDRepository
) {
    suspend operator fun invoke(fdId: String): NetworkResponse<FDDetailsDomain, NetworkError> {
        return repository.getFDDetails(fdId)
    }
}
