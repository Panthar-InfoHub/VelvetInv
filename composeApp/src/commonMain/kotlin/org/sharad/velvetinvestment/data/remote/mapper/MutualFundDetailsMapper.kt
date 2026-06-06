package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.mfdetails.Metrics
import org.sharad.velvetinvestment.data.remote.model.mfdetails.MutualFundsDetailDto
import org.sharad.velvetinvestment.domain.models.mutualfunds.InvestmentFrequency
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain

fun MutualFundsDetailDto.toDomain(): MutualFundDetailsDomain {
    val d = data

    return MutualFundDetailsDomain(
        amc_code = d.amc_code?:"",
        amc_id = d.amc_id?:"",
        amc_name = d.amc_name?:"",
        asset_type = d.asset_type?:"",
        createdAt = d.createdAt?:"",
        id = d.id,
        isin = d.isin,
        latest_nav = d.latest_nav?:"n/a",
        latest_nav_date = d.latest_nav_date?:"",
        mapping_code = d.mapping_code?:"",
        maturity_date = d.maturity_date,
        metrics = d.metrics?: Metrics(0.0, null, null, null, null,null, null),
        nfo_end_date = d.nfo_end_date,
        nse_scheme_code = d.nse_scheme_code?:"",
        platform_code = d.platform_code?:"",
        purchase_allowed = d.purchase_allowed?: true,
        redemption_allowed = d.redemption_allowed?: true,
        risk_level = d.risk_level?:-1,
        risk_name = d.risk_name?:"n/a",
        scheme_id = d.scheme_id?:"",
        scheme_name = d.scheme_name?:"",
        scheme_type = d.scheme_type?:"",
        sip_allowed = d.sip_allowed?:false,
        structure = d.structure?:"",
        switch_allowed = d.switch_allowed?: false,
        sipAllowedDated = d.transaction_rules?.sip_allowed_dates?:emptyList(),
        investmentFrequency = d.transaction_rules?.sip_frequencies?.mapNotNull { InvestmentFrequency.fromCode(it) }?: emptyList(),
        updatedAt = d.updatedAt?:"",
        icon = d.img_url?:"",
        minAmount = d.transaction_rules?.min_investment_amount?.toLong()?:0,
        minSipAmount = d.transaction_rules?.min_sip_amount?.toLong()?:0,
        minLumpSumAmount = d.transaction_rules?.min_lump_sum_amount?.toLong()?:0,
    )
}