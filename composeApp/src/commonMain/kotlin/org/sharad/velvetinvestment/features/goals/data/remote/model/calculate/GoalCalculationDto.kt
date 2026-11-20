package org.sharad.velvetinvestment.features.goals.data.remote.model.calculate

import kotlinx.serialization.Serializable

/** `POST /user-goal/calculate` — the projection for inputs the user has not committed to yet. */
@Serializable
data class GoalCalculationResponseDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: GoalCalculationDto
)

@Serializable
data class GoalCalculationDto(
    val goal_type_id: Int,
    val years_remaining: Int = 0,
    val current_cost: Double? = null,
    val target_amount: Double? = null,
    val current_savings: Double = 0.0,
    /** The rates actually applied — the type's defaults unless the request overrode them. */
    val inflation_rate_used: Double? = null,
    val expected_return_rate_used: Double? = null,
    val future_target_amount: Double = 0.0,
    val fv_current_savings: Double = 0.0,
    val net_required_corpus: Double = 0.0,
    val required_monthly_sip: Double = 0.0,
    val required_lumpsum_today: Double = 0.0,
    /** Retirement (type 7) only — that type's calculate response has its own fields. */
    val current_age: Int? = null,
    val retirement_age: Int? = null,
    val plan_until_age: Int? = null,
    val years_to_retirement: Int? = null,
    val monthly_cost_at_retirement: Double? = null,
    val target_retirement_corpus: Double? = null,
    val fv_existing_savings: Double? = null,
    val remaining_corpus_to_build: Double? = null,
    val total_monthly_sip: Double? = null,
    val additional_lumpsum_today: Double? = null,
    val pre_retirement_return_used: Double? = null,
    val inflation_before_used: Double? = null
)
