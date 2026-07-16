package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.models.notifications.NotificationDomain
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetNotificationsUseCase(private val repository: UserAuth) {
    suspend operator fun invoke(): NetworkResponse<List<NotificationDomain>, ErrorDomain> {
        return repository.getNotifications()
    }
}
