package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class ComputedMetrics(
    val total_assets: Long,
    val total_liabilities: Long,
    val net_worth: Long,

    val monthly_income: Double,
    val total_annual_expenses: Long,
    val annual_savings: Long,

    val savings_rate: Double,
    val monthly_available_surplus: Double,

    val liquid_assets: Long,
    val illiquid_assets: Long,

    val total_monthly_emi: Long,
    val debt_to_income_ratio: Double
)