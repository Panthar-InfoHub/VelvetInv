package org.sharad.velvetinvestment.features.notifications.data.remote.mapper

import org.sharad.velvetinvestment.features.notifications.data.remote.model.NotificationDto
import org.sharad.velvetinvestment.features.notifications.data.remote.model.NotificationResponseDto
import org.sharad.velvetinvestment.features.notifications.domain.model.NotificationDomain
import org.sharad.velvetinvestment.features.notifications.domain.model.NotificationSubType

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
