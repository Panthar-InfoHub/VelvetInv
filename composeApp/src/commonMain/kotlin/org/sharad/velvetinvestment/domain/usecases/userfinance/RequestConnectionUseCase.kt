package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class RequestConnectionUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(type: String, message: String = ""): NetworkResponse<Unit, ErrorDomain> {
        return repository.requestConnection(type, message)
    }
}
