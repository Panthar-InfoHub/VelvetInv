package org.sharad.velvetinvestment.domain.models.mutualfunds

import org.sharad.velvetinvestment.data.remote.model.mfdetails.Metrics

data class MutualFundDetailsDomain(
    val amc_code: String,
    val amc_id: String,
    val amc_name: String,
    val asset_type: String,
    val createdAt: String,
    val id: String,
    val isin: String?,
    val latest_nav: String,
    val latest_nav_date: String,
    val mapping_code: String,
    val maturity_date: String?,
    val metrics: Metrics,
    val nfo_end_date: String?,
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
    val sipAllowedDated: List<Int>,
    val investmentFrequency: List<InvestmentFrequency>,
    val updatedAt: String,
    val icon: String,
    val minAmount: Long,
    val minSipAmount: Long,
    val minLumpSumAmount: Long
)

enum class InvestmentFrequency(
    val code: String,
    val label: String
) {
    DAILY_Z("DZ", "Daily"),
    DAILY("D", "Daily"),

    MONTHLY("OM", "Monthly"),
    QUARTERLY("Q", "Quarterly"),

    WEEKLY("WD", "Weekly"),
    ONCE_A_WEEK("OW", "Once a Week"),

    HALF_YEARLY("H", "Half Yearly"),
    YEARLY("Y", "Yearly");

    companion object {

        fun fromCode(code: String?): InvestmentFrequency? {
            return entries.find { it.code.equals(code, ignoreCase = true) }
        }

        fun fromLabel(label: String?): InvestmentFrequency? {
            return entries.find { it.label.equals(label, ignoreCase = true) }
        }
    }
}