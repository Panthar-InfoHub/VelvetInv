package org.sharad.velvetinvestment.data.remote.model.mfdetails

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRules(
    val sip_allowed_dates: List<Int>,
    val sip_frequencies: List<String>
)