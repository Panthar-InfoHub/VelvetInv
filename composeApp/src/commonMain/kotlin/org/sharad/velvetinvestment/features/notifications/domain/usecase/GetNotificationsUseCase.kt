package org.sharad.velvetinvestment.features.notifications.domain.usecase

import org.sharad.velvetinvestment.features.notifications.domain.model.NotificationDomain
import org.sharad.velvetinvestment.features.notifications.domain.repository.NotificationRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetNotificationsUseCase(private val repository: NotificationRepository) {
    suspend operator fun invoke(): NetworkResponse<List<NotificationDomain>, ErrorDomain> {
        return repository.getNotifications()
    }
}
