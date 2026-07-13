package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.mutualfundcombined.BundleMetaData
import org.sharad.velvetinvestment.data.remote.model.mutualfundcombined.BundleProduct
import org.sharad.velvetinvestment.data.remote.model.mutualfundcombined.CombinedFundsDto
import org.sharad.velvetinvestment.data.remote.model.mutualfundcombined.Item
import org.sharad.velvetinvestment.data.remote.model.mutualfundcombined.ItemXX
import org.sharad.velvetinvestment.data.remote.model.mutualfundcombined.Metrics
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundleMetaDataDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundItemDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CombinedFundsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CuratedBundleDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.ReturnYearsRateDomain
import org.sharad.velvetinvestment.presentation.mutualfund.CategoryMutualFundDomain
import org.sharad.velvetinvestment.utils.toTitleCase
import org.sharad.velvetinvestment.utils.trimDoubleTo

fun CombinedFundsDto.toDomain(): CombinedFundsDomain {
    return CombinedFundsDomain(
        bundleFunds = data.bundle_funds.items.map { it.toDomain() },
        categoryMutualFundDomain = data.normal_funds.items.map { itemX ->
            CategoryMutualFundDomain(
                categorySearchReference = itemX.key,
                categoryName = itemX.title,
                mutualFunds = itemX.items.map { it.toDomain() },
            )
        }
    )
}

fun Item.toDomain(): CuratedBundleDomain {
    return CuratedBundleDomain(
        id = id,
        name = bundle_name,
        description = bundle_description,
        equityPercentage = equity_percentage,
        commodityPercentage = commodity_percentage,
        debtPercentage = debt_percentage,
        hybridPercentage = hybrid_percentage,
        metaData = meta_data.toDomain()
    )
}

fun BundleMetaData.toDomain(): BundleMetaDataDomain {
    return BundleMetaDataDomain(
        riskLevel = risk_level,
        investmentTime = investment_time,
        investmentGrowth = investment_growth
    )
}

fun BundleProduct.toDomain(): BundledMutualFundItemDomain {
    val product = this.mf_product

    return BundledMutualFundItemDomain(
        id = product.id,
        scheme_id = product.scheme_id,
        isin = product.isin,
        mapping_code = product.mapping_code,
        nse_scheme_code = product.nse_scheme_code,
        platform_code = product.platform_code,
        scheme_name = product.scheme_name.toTitleCase(),
        amc_id = product.amc_id,
        amc_code = product.amc_code,
        amc_name = product.amc_name,
        asset_type = product.asset_type,
        scheme_type = product.scheme_type,
        structure = product.structure,
        risk_name = product.risk_name,
        risk_level = product.risk_level,
        latest_nav = product.latest_nav,
        latest_nav_date = product.latest_nav_date,
        purchase_allowed = product.purchase_allowed,
        sip_allowed = product.sip_allowed,
        redemption_allowed = product.redemption_allowed,
        switch_allowed = product.switch_allowed,
        maturity_date = product.maturity_date,
        nfo_end_date = product.nfo_end_date,
        createdAt = product.createdAt,
        updatedAt = product.updatedAt,
        allocation_percentage = allocation_percentage,
        minAmount = min_amount,
        icon = product.img_url ?: ""
    )
}


fun ItemXX.toDomain(): MutualFundDomain {
    return MutualFundDomain(
        id = id,
        name = scheme_name.toTitleCase(),
        icon = img_url ?: "",
        category = asset_type,
        remark = null,
        riskText = risk_name,
        type = scheme_type,
        returnYearsRate = metrics.toDomain(),
        latestNav = latest_nav,
        shortName1 = display_name_001 ?: "",
        shortName2 = display_name_002 ?: "",
    )
}

fun Metrics.toDomain(): ReturnYearsRateDomain {
    return ReturnYearsRateDomain(
        month3 = return_90d?.trimDoubleTo(2),
        month6 = return_6m?.trimDoubleTo(2),
        year1 = return_1y?.trimDoubleTo(2),
        year3 = return_3y?.trimDoubleTo(2),
    )
}
