package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class ComputedMetrics(
    val annual_savings: Int,
    val debt_to_income_ratio: Int,
    val illiquid_assets: Int,
    val liquid_assets: Int,
    val monthly_available_surplus: Int,
    val monthly_income: Int,
    val net_worth: Int,
    val savings_rate: Int,
    val total_annual_expenses: Int,
    val total_assets: Int,
    val total_liabilities: Int,
    val total_monthly_emi: Int
)