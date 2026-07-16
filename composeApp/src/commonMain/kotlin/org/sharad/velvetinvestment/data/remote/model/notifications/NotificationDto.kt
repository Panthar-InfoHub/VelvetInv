package org.sharad.velvetinvestment.data.remote.model.notifications

import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponseDto(
    val success: Boolean,
    val message: String,
    val data: NotificationDataDto
)

@Serializable
data class NotificationDataDto(
    val total: Int,
    val page: Int,
    val limit: Int,
    val notifications: List<NotificationDto>
)

@Serializable
data class NotificationDto(
    val id: String,
    val user_id: String,
    val type: String,
    val title: String,
    val body: String,
    val payload: NotificationPayloadDto,
    val is_read: Boolean,
    val createdAt: String,
    val readAt: String?
)

@Serializable
data class NotificationPayloadDto(
    val txn: String? = null,
    val sub_type: String? = null
)

@Serializable
data class UnreadStatusResponseDto(
    val success: Boolean,
    val message: String,
    val data: UnreadStatusDataDto
)

@Serializable
data class UnreadStatusDataDto(
    val has_unread: Boolean
)
