package org.sharad.velvetinvestment.features.investmentrate.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val average_savings_pattern: AverageSavingsPattern,
    val investing_trend: List<InvestingTrend>,
    val spending_categories: SpendingCategories
)