package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetUnreadStatusUseCase(private val repository: UserAuth) {
    suspend operator fun invoke(): NetworkResponse<Boolean, ErrorDomain> {
        return repository.getUnreadStatus()
    }
}
