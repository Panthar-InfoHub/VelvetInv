package org.sharad.velvetinvestment.features.mutualfund.data.remote.model.mfdetails

import kotlinx.serialization.Serializable

/** The fund's plan and transaction rules. Amounts come as strings, e.g. "100". */
@Serializable
data class SchemePlan(
    val id: String? = null,
    val scheme_name: String? = null,
    val fund_name: String? = null,
    val gateway: String? = null,
    val plan_type: String? = null,
    val option: String? = null,
    val idcw_option: String? = null,
    val active: Boolean? = null,

    val lumpsum_allowed: Boolean? = null,
    val lumpsum_amount_min: String? = null,
    val lumpsum_amount_max: String? = null,
    val lumpsum_amount_multiples: String? = null,
    val lumpsum_additional_amount_min: String? = null,

    val withdrawal_allowed: Boolean? = null,
    val withdrawal_amount_min: String? = null,
    val withdrawal_amount_max: String? = null,
    val withdrawal_amount_multiples: String? = null,
    val withdrawal_units_min: String? = null,
    val withdrawal_units_multiples: String? = null,

    val sip_daily_allowed: Boolean? = null,
    val sip_daily_amount_min: String? = null,
    val sip_daily_amount_max: String? = null,
    val sip_daily_amount_multiples: String? = null,
    val sip_daily_installments_min: Int? = null,

    val sip_monthly_allowed: Boolean? = null,
    val sip_monthly_amount_min: String? = null,
    val sip_monthly_amount_max: String? = null,
    val sip_monthly_amount_multiples: String? = null,
    val sip_monthly_installments_min: Int? = null,
    val sip_monthly_dates: List<Int> = emptyList(),

    val fund_category: String? = null,
    val sub_category: String? = null,
    val amfi_code: String? = null,
    val amc_id: Int? = null,
    val close_ended: Boolean? = null,
    val lock_in: Boolean? = null,
    val lock_in_period: String? = null,

    val purchase_allowed_v1: Boolean? = null,
    val redemption_allowed_v1: Boolean? = null,
    val instant_redemption_allowed: Boolean? = null,
    val sip_allowed: Boolean? = null,
    val stp_in_allowed: Boolean? = null,
    val stp_out_allowed: Boolean? = null,

    val switch_in_allowed: Boolean? = null,
    val switch_in_amount_min: String? = null,
    val switch_in_amount_multiples: String? = null,
    val switch_out_allowed: Boolean? = null,
    val switch_out_amount_min: String? = null,
    val switch_out_amount_multiples: String? = null,
    val switch_out_units_min: String? = null,
    val switch_out_unit_multiples: String? = null,

    /** Keyed by frequency: daily, monthly, quarterly, half_yearly, yearly, day_in_a_week, calendar_day_daily, … */
    val sip_frequency_data: Map<String, FrequencyRule> = emptyMap(),
    /** Same shape as [sip_frequency_data]; also has four_times_a_month. */
    val stp_frequency_data: Map<String, FrequencyRule> = emptyMap(),

    val last_synced_at: String? = null,
    val v1_synced_at: String? = null
)

@Serializable
data class FrequencyRule(
    val dates: List<Int> = emptyList(),
    val amount_multiples: Double? = null,
    val min_installments: Int? = null,
    val max_installment_amount: Double? = null,
    val min_installment_amount: Double? = null
)
