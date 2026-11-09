package org.sharad.velvetinvestment.features.notifications.domain.repository

import org.sharad.velvetinvestment.features.notifications.domain.model.NotificationDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface NotificationRepository {
    suspend fun getNotifications(): NetworkResponse<List<NotificationDomain>, ErrorDomain>

    /** True when at least one notification is unread (drives the home bell badge). */
    suspend fun getUnreadStatus(): NetworkResponse<Boolean, ErrorDomain>

    suspend fun markNotificationsAsRead(): NetworkResponse<Unit, ErrorDomain>
}
