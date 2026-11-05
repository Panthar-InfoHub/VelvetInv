package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class ComputedMetrics(
    val total_assets: Double = 0.0,
    val total_liabilities: Double = 0.0,
    val net_worth: Double = 0.0,

    val monthly_income: Double = 0.0,
    val total_annual_expenses: Double = 0.0,
    val annual_savings: Double = 0.0,

    val savings_rate: Double = 0.0,
    val monthly_available_surplus: Double = 0.0,

    val liquid_assets: Double = 0.0,
    val illiquid_assets: Double = 0.0,

    val total_monthly_emi: Double = 0.0,
    val debt_to_income_ratio: Double = 0.0
)
