package org.sharad.velvetinvestment.data.remote.model.bundledfundbyid

import kotlinx.serialization.Serializable

@Serializable
data class Fund(
    val amc_code: String,
    val amc_id: String,
    val amc_name: String,
    val asset_type: String,
    val createdAt: String,
    val display_name_001: String,
    val display_name_002: String,
    val id: String,
    val img_url: String,
    val isin: String,
    val latest_nav: String,
    val latest_nav_date: String,
    val mapping_code: String,
    val maturity_date: String? = null,
    val metrics: Metrics,
    val nfo_end_date: String? = null,
    val nse_scheme_code: String,
    val platform_code: String,
    val purchase_allowed: Boolean,
    val redemption_allowed: Boolean,
    val risk_level: Int,
    val risk_name: String,
    val scheme_id: String,
    val scheme_name: String,
    val scheme_type: String,
    val sip_allowed: Boolean,
    val structure: String,
    val switch_allowed: Boolean,
    val transaction_rules: TransactionRules,
    val updatedAt: String
)

@Serializable
data class TransactionRules(
    val id: String,
    val mf_product_id: String,
    val min_sip_amount: String,
    val min_lump_sum_amount: String,
    val sip_allowed_dates: List<Int>,
    val sip_frequencies: List<String>,
    val min_investment_amount: String,
    val min_lumpsum_add_on_amount: String,
    val min_redem_qty: String,
    val min_redem_amount: String,
    val min_daily_sip_amount: String,
    val min_weekly_sip_amount: String,
    val min_fortnightly_sip_amount: String,
    val min_monthly_sip_amount: String,
    val min_quarterly_sip_amount: String,
    val min_semi_annual_sip_amount: String,
    val min_annual_sip_amount: String,
    val createdAt: String,
    val updatedAt: String
)