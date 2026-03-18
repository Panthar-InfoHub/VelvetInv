package org.sharad.velvetinvestment.data.remote.model.onboarding

import kotlinx.serialization.Serializable

@Serializable
data class Assets(
    val cash_saving: Long,
    val fd: Long,
    val gold: Long,
    val mutual_funds: Long,
    val real_estate: Long,
    val stocks: Long
)