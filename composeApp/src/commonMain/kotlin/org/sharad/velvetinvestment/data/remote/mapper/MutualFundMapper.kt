package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.getmf.Metrics
import org.sharad.velvetinvestment.data.remote.model.getmf.MutualFund
import org.sharad.velvetinvestment.data.remote.model.getmf.MutualFundDto
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.ReturnYearsRateDomain

fun MutualFund.toDomain(): MutualFundDomain {
    return MutualFundDomain(
        id = id,
        name = scheme_name,
        icon = "",
        category = asset_type,
        remark = null,
        riskText = risk_name,
        type = scheme_type,
        returnYearsRate = metrics.toReturnDomain()
    )
}

fun Metrics.toReturnDomain(): ReturnYearsRateDomain {
    return ReturnYearsRateDomain(
        month3 = return_90d,
        month6 = return_6m,
        year1 = return_1y,
        year3 = return_3y
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