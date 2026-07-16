package org.sharad.velvetinvestment.data.remote.model.allbundles

import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain

fun AllBundlesDto.toDomain(): PaginatedData<BundledMutualFundDomain> {
    return PaginatedData(
        items = data.bundles.map { bundle ->
            BundledMutualFundDomain(
                id = bundle.id,
                bundleName = bundle.bundleName,
                bundleDescription = bundle.bundleDescription,
                equityPercentage = bundle.equityPercentage,
                commodityPercentage = bundle.commodityPercentage,
                debtPercentage = bundle.debtPercentage,
                hybridPercentage = bundle.hybridPercentage,
                riskLevel = bundle.metaData.riskLevel,
                investmentTime = bundle.metaData.investmentTime,
                investmentGrowth = bundle.metaData.investmentGrowth
            )
        },
        page = data.pagination.page,
        pageSize = data.pagination.limit,
        totalItems = data.pagination.total,
        totalPages = data.pagination.totalPages,
        hasNextPage = data.pagination.page < data.pagination.totalPages
    )
}