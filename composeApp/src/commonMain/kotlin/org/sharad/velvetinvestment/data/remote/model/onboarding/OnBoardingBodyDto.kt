package org.sharad.velvetinvestment.data.remote.model.onboarding

import kotlinx.serialization.Serializable

@Serializable
data class OnBoardingBodyDto(
    val assets: Assets,
    val finance: Finance,
    val goals: List<GoalRequestDto>,
    val insurance: Insurance,
    val loans: List<Loan>,
    val profile: Profile
)