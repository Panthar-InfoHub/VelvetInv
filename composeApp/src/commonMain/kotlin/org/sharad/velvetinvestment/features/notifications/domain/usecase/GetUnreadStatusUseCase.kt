package org.sharad.velvetinvestment.features.notifications.domain.usecase

import org.sharad.velvetinvestment.features.notifications.domain.repository.NotificationRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetUnreadStatusUseCase(private val repository: NotificationRepository) {
    suspend operator fun invoke(): NetworkResponse<Boolean, ErrorDomain> {
        return repository.getUnreadStatus()
    }
}
