package org.sharad.velvetinvestment.features.mutualfund.data.remote.mapper

import org.sharad.velvetinvestment.features.mutualfund.data.remote.model.mfdetails.MutualFundsDetailDto
import org.sharad.velvetinvestment.features.mutualfund.data.remote.model.mfdetails.SchemePlan
import org.sharad.velvetinvestment.features.mutualfund.domain.models.InvestmentFrequency
import org.sharad.velvetinvestment.features.mutualfund.domain.models.MutualFundDetailsDomain
import org.sharad.velvetinvestment.features.mutualfund.domain.models.Metrics as DomainMetrics

/**
 * `GET /mf/{id}` no longer sends risk, AMC code, scheme id or a flat transaction_rules block;
 * rules now come from `scheme_plan`. Fields with no source keep the old fallbacks.
 */
fun MutualFundsDetailDto.toDomain(): MutualFundDetailsDomain {
    val d = data
    val plan = d.scheme_plan
    val category = plan?.fund_category.orEmpty()
    val minLumpSum = plan?.lumpsum_amount_min.toAmount()
    val minSip = plan?.sip_monthly_amount_min.toAmount()
        ?: plan?.sip_frequency_data?.get("monthly")?.min_installment_amount?.toLong()

    return MutualFundDetailsDomain(
        amc_code = "",
        amc_id = plan?.amc_id?.toString().orEmpty(),
        amc_name = plan?.fund_name.orEmpty(),
        asset_type = category,
        createdAt = d.createdAt.orEmpty(),
        id = d.id,
        isin = d.isin,
        latest_nav = d.latest_nav ?: "n/a",
        latest_nav_date = d.latest_nav_date.orEmpty(),
        mapping_code = plan?.amfi_code.orEmpty(),
        maturity_date = null,
        metrics = d.metrics?.let {
            DomainMetrics(
                nav_change_pct = it.nav_change_pct,
                return_1y = it.return_1y,
                return_30d = it.return_30d,
                return_3y = it.return_3y,
                return_6m = it.return_6m,
                return_90d = it.return_90d,
                return_5y = it.return_5y
            )
        } ?: DomainMetrics(0.0, null, null, null, null, null, null),
        nfo_end_date = null,
        nse_scheme_code = d.scheme_code?.toString().orEmpty(),
        platform_code = plan?.gateway.orEmpty(),
        purchase_allowed = plan?.let { (it.lumpsum_allowed ?: true) && (it.active ?: true) } ?: true,
        redemption_allowed = plan?.withdrawal_allowed ?: true,
        risk_level = -1,
        risk_name = "n/a",
        scheme_id = plan?.id.orEmpty(),
        scheme_name = d.name ?: plan?.scheme_name.orEmpty(),
        scheme_type = category,
        sip_allowed = plan?.sip_allowed ?: false,
        structure = if (plan?.close_ended == true) "Close Ended" else "Open Ended",
        switch_allowed = plan?.switch_out_allowed ?: false,
        sipAllowedDated = plan?.sip_monthly_dates?.takeIf { it.isNotEmpty() }
            ?: plan?.sip_frequency_data?.get("monthly")?.dates.orEmpty(),
        investmentFrequency = plan?.sipFrequencies().orEmpty(),
        updatedAt = d.updatedAt.orEmpty(),
        icon = d.img_url.orEmpty(),
        minAmount = minLumpSum ?: minSip ?: 0,
        minSipAmount = minSip ?: 0,
        minLumpSumAmount = minLumpSum ?: 0,
    )
}

/** "100" / "0.01" → whole rupees; null when missing or unparseable. */
private fun String?.toAmount(): Long? = this?.toDoubleOrNull()?.toLong()

/** SIP frequencies the plan offers, in [InvestmentFrequency] order, honouring the *_allowed flags. */
private fun SchemePlan.sipFrequencies(): List<InvestmentFrequency> {
    if (sip_allowed == false) return emptyList()
    val keys = sip_frequency_data.keys
    return buildList {
        // calendar_day_daily is the same "Daily" option; only use it when plain daily is absent.
        if (sip_daily_allowed != false) {
            when {
                "daily" in keys -> add(InvestmentFrequency.DAILY)
                "calendar_day_daily" in keys -> add(InvestmentFrequency.DAILY_Z)
            }
        }
        if ("day_in_a_week" in keys) add(InvestmentFrequency.WEEKLY)
        if (sip_monthly_allowed != false && ("monthly" in keys || sip_monthly_dates.isNotEmpty())) {
            add(InvestmentFrequency.MONTHLY)
        }
        if ("quarterly" in keys) add(InvestmentFrequency.QUARTERLY)
        if ("half_yearly" in keys) add(InvestmentFrequency.HALF_YEARLY)
        if ("yearly" in keys) add(InvestmentFrequency.YEARLY)
    }
}
