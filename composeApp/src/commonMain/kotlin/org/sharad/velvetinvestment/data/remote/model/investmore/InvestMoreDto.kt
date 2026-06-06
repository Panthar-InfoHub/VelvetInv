package org.sharad.velvetinvestment.data.remote.model.investmore

import kotlinx.serialization.Serializable

@Serializable
data class InvestMoreDto(
    val type: String,
    val items: List<InvestMoreItemDto>
)

@Serializable
data class InvestMoreItemDto(
    val scheme_id: String,
    val amount: Long,
    val folio: String
)
