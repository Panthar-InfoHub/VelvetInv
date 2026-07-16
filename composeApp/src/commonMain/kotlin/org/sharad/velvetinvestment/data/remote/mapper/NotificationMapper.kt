package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.notifications.NotificationDto
import org.sharad.velvetinvestment.data.remote.model.notifications.NotificationResponseDto
import org.sharad.velvetinvestment.domain.models.notifications.NotificationDomain
import org.sharad.velvetinvestment.domain.models.notifications.NotificationSubType

fun NotificationResponseDto.toDomain(): List<NotificationDomain> {
    return data.notifications.map { it.toDomain() }
}

fun NotificationDto.toDomain(): NotificationDomain {
    return NotificationDomain(
        id = id,
        title = title,
        body = body,
        subType = when (payload.sub_type) {
            "ALERT" -> NotificationSubType.ALERT
            "FUND_INC" -> NotificationSubType.FUND_INC
            "FUND_DEC" -> NotificationSubType.FUND_DEC
            "REMINDER" -> NotificationSubType.REMINDER
            "NOTIFICATION" -> NotificationSubType.NOTIFICATION
            else -> NotificationSubType.UNKNOWN
        },
        createdAt = createdAt,
        isRead = is_read
    )
}
