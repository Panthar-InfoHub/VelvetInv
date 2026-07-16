package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.getmf.Metrics
import org.sharad.velvetinvestment.data.remote.model.getmf.MutualFund
import org.sharad.velvetinvestment.data.remote.model.getmf.MutualFundDto
import org.sharad.velvetinvestment.data.remote.model.initiatemfpurchase.Data
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.mutualfunds.MandateStatus
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundPurchaseInitiateDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.ReturnYearsRateDomain
import org.sharad.velvetinvestment.utils.toTitleCase
import org.sharad.velvetinvestment.utils.trimDoubleTo

fun MutualFund.toDomain(): MutualFundDomain {
    return MutualFundDomain(
        id = id,
        name = scheme_name.toTitleCase(),
        icon = img_url?:"",
        category = asset_type?:"",
        remark = null,
        riskText = risk_name,
        riskLevel = risk_level,
        type = scheme_type?:"",
        returnYearsRate = metrics.toReturnDomain(),
        latestNav = latest_nav?: "n/a",
        shortName1 = display_name_001?:"",
        shortName2 = display_name_002?:""
    )
}

fun Metrics?.toReturnDomain(): ReturnYearsRateDomain {
    return this?.let {
        ReturnYearsRateDomain(
            month3 = return_90d?.trimDoubleTo(2),
            month6 = return_6m?.trimDoubleTo(2),
            year1 = return_1y?.trimDoubleTo(2),
            year3 = return_3y?.trimDoubleTo(2),
            year5 = return_5y?.trimDoubleTo(2)
        )
    }?: ReturnYearsRateDomain(
        month3 = null,
        month6 = null,
        year1 = null,
        year3 = null,
        year5 = null
    )
}

fun MutualFundDto.toPaginatedDomain(): PaginatedData<MutualFundDomain> {
    val paginationData = data.pagination

    return PaginatedData(
        items = data.mutual_funds.map { it.toDomain() },

        page = paginationData.page,
        pageSize = paginationData.limit,
        totalItems = paginationData.total,
        totalPages = paginationData.totalPages,

        hasNextPage = paginationData.page < paginationData.totalPages
    )
}
//fun BundledFundByIdDto.toDomain(): BundledMutualFundDomain {
//    return BundledMutualFundDomain(
//        categoryName = data.bundle_name,
//        key = data.id,
//        img_url = data.img_url?:"",
//        allowedFrequencies = data.allowed_frequencies.mapNotNull {
//            InvestmentFrequency.fromCode(it)
//        },
//        minAmount = data.accumulated_min_amount,
//        sipDates = data.allowed_dates,
//        mutualFunds = data.bundle_products.map { bundleProduct ->
//            val mf = bundleProduct.mf_product
//
//        }
//    )
//}

fun Data.toDomain(): MutualFundPurchaseInitiateDomain {
    return MutualFundPurchaseInitiateDomain(
        mandateId = mandate_id,
        url = mandate_short_url,
        status = if (status=="MANDATE_APPROVED") MandateStatus.APPROVED else MandateStatus.PENDING,
    )
}