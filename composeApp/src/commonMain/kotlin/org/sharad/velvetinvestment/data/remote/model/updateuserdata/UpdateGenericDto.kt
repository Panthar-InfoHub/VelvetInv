package org.sharad.velvetinvestment.data.remote.model.updateuserdata

import org.sharad.velvetinvestment.data.remote.model.onboarding.Assets
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import org.sharad.velvetinvestment.data.remote.model.onboarding.Finance
import org.sharad.velvetinvestment.data.remote.model.onboarding.Insurance
import org.sharad.velvetinvestment.data.remote.model.onboarding.Loan
import org.sharad.velvetinvestment.data.remote.model.onboarding.Profile

@Serializable
data class AssetUpdateDto(val assets: Assets)

@kotlinx.serialization.Serializable
data class FinanceUpdateDto(val finance: Finance)

@Serializable
data class InsuranceUpdateDto(val insurance: Insurance)

@Serializable
data class LoanUpdateDto(val loans: List<Loan>)

@Serializable
data class ProfileUpdateDto(val profile: Profile)

@Serializable
data class GoalsUpdateDto(val goals: List<JsonElement>)