package org.sharad.velvetinvestment.data.remote.model.onboarding

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class OnBoardingBodyDto(
    val assets: Assets,
    val finance: Finance,
    val goals: List<JsonElement>,
    val insurance: Insurance,
    val loans: List<Loan>,
    val profile: Profile
)