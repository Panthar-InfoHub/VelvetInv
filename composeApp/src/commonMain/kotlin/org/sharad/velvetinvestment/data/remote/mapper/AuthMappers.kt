package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.auth.verifyotp.VerifyOtpDto
import org.sharad.velvetinvestment.domain.models.auth.LoginDomain

fun VerifyOtpDto.toLoginDto(): LoginDomain {
    return LoginDomain(
        onboarded = this.data.user.metadata.is_onboarding_completed,
        onboardingStep = this.data.user.metadata.onboarding_stage,
        userId = this.data.user.user_id
    )
}