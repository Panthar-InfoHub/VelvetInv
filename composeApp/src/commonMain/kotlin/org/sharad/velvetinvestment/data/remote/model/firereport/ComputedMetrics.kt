package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class ComputedMetrics(
    val total_assets: Double,
    val total_liabilities: Double,
    val net_worth: Double,

    val monthly_income: Double,
    val total_annual_expenses: Double,
    val annual_savings: Double,

    val savings_rate: Double,
    val monthly_available_surplus: Double,

    val liquid_assets: Double,
    val illiquid_assets: Double,

    val total_monthly_emi: Double,
    val debt_to_income_ratio: Double
)