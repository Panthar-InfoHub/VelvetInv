package org.sharad.velvetinvestment.data.remote.model.investmentratedto

import kotlinx.serialization.Serializable

@Serializable
data class SpendingCategories(
    val essentials: Essentials,
    val investments: Investments,
    val savings: Savings
)