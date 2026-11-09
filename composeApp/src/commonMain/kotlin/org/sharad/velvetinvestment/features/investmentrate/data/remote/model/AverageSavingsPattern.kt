package org.sharad.velvetinvestment.features.investmentrate.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AverageSavingsPattern(
    val current_savings_percent: Double,
    val month_over_month_delta: Double,
    val previous_month_savings_percent: Double,
    val total_saved_vs_prev_month: Long
)