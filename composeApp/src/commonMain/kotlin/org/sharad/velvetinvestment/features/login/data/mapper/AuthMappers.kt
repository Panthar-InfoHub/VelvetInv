package org.sharad.velvetinvestment.features.login.data.mapper

import org.sharad.velvetinvestment.features.login.data.models.auth.verifyotp.VerifyOtpDto
import org.sharad.velvetinvestment.features.login.domain.model.LoginDomain

/** The server decides main app vs onboarding: `onboarding.can_navigate_home`. */
fun VerifyOtpDto.toLoginDomain(): LoginDomain = LoginDomain(
    canEnterMainApp = data.onboarding.can_navigate_home,
    userId = data.user.user_id
)
