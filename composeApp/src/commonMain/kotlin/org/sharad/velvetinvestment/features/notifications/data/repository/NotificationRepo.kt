package org.sharad.velvetinvestment.features.notifications.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import org.sharad.velvetinvestment.features.notifications.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.features.notifications.data.remote.model.NotificationResponseDto
import org.sharad.velvetinvestment.features.notifications.data.remote.model.UnreadStatusResponseDto
import org.sharad.velvetinvestment.features.notifications.domain.model.NotificationDomain
import org.sharad.velvetinvestment.features.notifications.domain.repository.NotificationRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.utils.networking.safeUnitRequest

class NotificationRepo(
    private val client: HttpClient
) : NotificationRepository {

    override suspend fun getNotifications(): NetworkResponse<List<NotificationDomain>, ErrorDomain> {
        val response = safeRequest<NotificationResponseDto> {
            client.get(getUrl("/user/notifications"))
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun getUnreadStatus(): NetworkResponse<Boolean, ErrorDomain> {
        val response = safeRequest<UnreadStatusResponseDto> {
            client.get(getUrl("/user/notifications/unread-status"))
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data.has_unread)
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun markNotificationsAsRead(): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.patch(getUrl("/user/notifications/read"))
        }
    }
}
