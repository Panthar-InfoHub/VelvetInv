package org.sharad.velvetinvestment.features.insurance.domain.usecase

import org.sharad.velvetinvestment.features.insurance.domain.repository.InsuranceRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class RequestConnectionUseCase(
    private val repository: InsuranceRepository
) {
    suspend operator fun invoke(type: String, message: String = ""): NetworkResponse<Unit, ErrorDomain> {
        return repository.requestConnection(type, message)
    }
}
