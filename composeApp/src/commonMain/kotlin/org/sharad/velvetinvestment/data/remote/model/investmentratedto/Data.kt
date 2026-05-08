package org.sharad.velvetinvestment.data.remote.model.investmentratedto

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val average_savings_pattern: AverageSavingsPattern,
    val investing_trend: List<InvestingTrend>,
    val spending_categories: SpendingCategories
)