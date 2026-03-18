package org.sharad.velvetinvestment.domain.models.auth

data class LoginDomain(
    val onboarded: Boolean,
    val onboardingStep:Int,
    val userId:String
)
