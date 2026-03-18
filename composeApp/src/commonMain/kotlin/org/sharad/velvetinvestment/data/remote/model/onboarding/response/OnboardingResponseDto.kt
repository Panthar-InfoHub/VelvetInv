package org.sharad.velvetinvestment.data.remote.model.onboarding.response

import kotlinx.serialization.Serializable

@Serializable
data class OnboardingResponseDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)