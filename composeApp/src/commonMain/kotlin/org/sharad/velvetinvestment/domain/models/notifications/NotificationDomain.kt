package org.sharad.velvetinvestment.domain.models.notifications

data class NotificationDomain(
    val id: String,
    val title: String,
    val body: String,
    val subType: NotificationSubType,
    val createdAt: String,
    val isRead: Boolean
)

enum class NotificationSubType {
    ALERT, FUND_INC, FUND_DEC, REMINDER, NOTIFICATION, UNKNOWN
}
