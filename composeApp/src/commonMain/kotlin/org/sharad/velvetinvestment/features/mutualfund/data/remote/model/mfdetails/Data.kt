package org.sharad.velvetinvestment.features.mutualfund.data.remote.model.mfdetails

import kotlinx.serialization.Serializable

/** `data` of `GET /mf/{id}`. Purchase/SIP rules live in [scheme_plan]. */
@Serializable
data class Data(
    val id: String = "",
    val name: String? = null,
    val isin: String? = null,
    val img_url: String? = null,
    val scheme_code: Long? = null,
    val latest_nav: String? = null,
    val latest_nav_date: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val metrics: Metrics? = null,
    val scheme_plan: SchemePlan? = null
)
