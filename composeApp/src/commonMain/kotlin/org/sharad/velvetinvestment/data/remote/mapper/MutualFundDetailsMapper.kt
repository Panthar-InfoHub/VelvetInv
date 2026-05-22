package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.mfdetails.Metrics
import org.sharad.velvetinvestment.data.remote.model.mfdetails.MutualFundsDetailDto
import org.sharad.velvetinvestment.domain.models.mutualfunds.InvestmentFrequency
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain

fun MutualFundsDetailDto.toDomain(): MutualFundDetailsDomain {
    val d = data

    return MutualFundDetailsDomain(
        amc_code = d.amc_code,
        amc_id = d.amc_id,
        amc_name = d.amc_name,
        asset_type = d.asset_type,
        createdAt = d.createdAt,
        id = d.id,
        isin = d.isin,
        latest_nav = d.latest_nav,
        latest_nav_date = d.latest_nav_date,
        mapping_code = d.mapping_code,
        maturity_date = d.maturity_date,
        metrics = d.metrics?: Metrics(0.0, null, null, null, null,null),
        nfo_end_date = d.nfo_end_date,
        nse_scheme_code = d.nse_scheme_code,
        platform_code = d.platform_code,
        purchase_allowed = d.purchase_allowed,
        redemption_allowed = d.redemption_allowed,
        risk_level = d.risk_level,
        risk_name = d.risk_name,
        scheme_id = d.scheme_id,
        scheme_name = d.scheme_name,
        scheme_type = d.scheme_type,
        sip_allowed = d.sip_allowed,
        structure = d.structure,
        switch_allowed = d.switch_allowed,
        sipAllowedDated = d.transaction_rules.sip_allowed_dates,
        investmentFrequency = d.transaction_rules.sip_frequencies.mapNotNull { InvestmentFrequency.fromCode(it) },
        updatedAt = d.updatedAt,
        icon = d.img_url?:"",
        minAmount = d.transaction_rules.min_investment_amount.toLong()
    )
}