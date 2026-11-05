package org.sharad.velvetinvestment.features.login.domain.model

data class LoginDomain(
    /** Whether the user goes straight to the main app: the server's `onboarding.can_navigate_home`. */
    val canEnterMainApp: Boolean,
    val userId: String
)
