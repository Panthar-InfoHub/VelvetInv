package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class MarkNotificationsAsReadUseCase(private val repository: UserAuth) {
    suspend operator fun invoke(): NetworkResponse<Unit, ErrorDomain> {
        return repository.markNotificationsAsRead()
    }
}
