package org.sharad.velvetinvestment.features.investmentrate.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SpendingCategories(
    val essentials: Essentials,
    val investments: Investments,
    val savings: Savings
)