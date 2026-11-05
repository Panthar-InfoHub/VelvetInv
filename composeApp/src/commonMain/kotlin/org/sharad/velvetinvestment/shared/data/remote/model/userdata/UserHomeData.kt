package org.sharad.velvetinvestment.shared.data.remote.model.userdata

import kotlinx.serialization.Serializable

/** `GET /user/` → `user_home_data`: the home screen's server-computed net worth and FIRE figures. */
@Serializable
data class UserHomeDataDto(
    val fire_number: EmiSplitDto? = null,
    val net_worth: Double = 0.0,
    val total_expenses: EmiSplitDto? = null,
    val fire_percentage: EmiSplitDto? = null
)

/** A figure reported both with and without loan EMIs counted as expenses. */
@Serializable
data class EmiSplitDto(
    val emi_include: Double = 0.0,
    val emi_exclude: Double = 0.0
)
